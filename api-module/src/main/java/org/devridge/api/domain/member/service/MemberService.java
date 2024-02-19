package org.devridge.api.domain.member.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.Role;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.devridge.api.domain.emailverification.exception.EmailVerificationInvalidException;
import org.devridge.api.domain.emailverification.repository.EmailVerificationRepository;
import org.devridge.api.domain.member.dto.request.*;
import org.devridge.api.domain.member.dto.response.MemberResponse;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.entity.Occupation;
import org.devridge.api.domain.member.exception.*;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.member.repository.OccupationRepository;
import org.devridge.api.domain.s3.dto.response.UploadImageResponse;
import org.devridge.api.domain.s3.service.S3Service;
import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.domain.skill.entity.key.MemberSkillId;
import org.devridge.api.domain.skill.repository.MemberSkillRepository;
import org.devridge.api.domain.skill.repository.SkillRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.AccessTokenUtil;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final OccupationRepository occupationRepository;
    private final S3Service s3Service;

    @Value("${devridge.s3.defaultImagePath}")
    private String defaultProfileImagePath;

    @Transactional
    public Long createMember(CreateMemberRequest memberRequest, MultipartFile image) throws IOException {
        checkDuplMembers(memberRequest);

        List<Skill> skills = fetchValidSkills(memberRequest.getSkillIds());
        Occupation occupation = validateOccupation(memberRequest.getOccupationId());

        UploadImageResponse imageResponse = uploadImageIfNeeded(new UploadImageResponse(defaultProfileImagePath), image);

        String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());
        Member member = buildAndSaveMember(memberRequest, occupation, imageResponse, encodedPassword);

        if (skills != null) {
            createMemberSkill(skills, member);
        }

        return member.getId();
    }

    @Transactional
    public void deleteMember(DeleteMemberRequest memberRequest) {
        Member member = getAuthenticatedMember();

        if (!passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException(400, "비밀번호가 틀렸습니다. 다시 입력해주세요.");
        }

        memberRepository.delete(member);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest passwordRequest) {
        checkTemporaryTokenForPasswordReset(passwordRequest.getTempJwt());

        EmailVerification emailVerification = checkEmailVerification(passwordRequest);

        Member member = memberRepository.findByEmailAndProvider(passwordRequest.getEmail(), "normal").orElseThrow(
                () -> new MemberNotFoundException(404, "해당 사용자를 찾을 수 없습니다.")
        );

        String encodedPassword = passwordEncoder.encode(passwordRequest.getPassword());
        member.changePassword(encodedPassword);

        emailVerificationRepository.delete(emailVerification);
    }

    @Transactional
    public void updatePassword(ChangePasswordRequest passwordRequest) {
        Member member = getAuthenticatedMember();

        String encodedPassword = passwordEncoder.encode(passwordRequest.getPassword());
        member.changePassword(encodedPassword);
    }

    @Transactional
    public void updateMember(UpdateProfileRequest updateProfileRequest, MultipartFile image) throws IOException {
        Member member = getAuthenticatedMember();

        fetchValidSkills(updateProfileRequest.getSkillIds());
        UploadImageResponse imageResponse = uploadImageIfNeeded(new UploadImageResponse(defaultProfileImagePath), image);

        member.setProfileImageUrl(imageResponse.getImagePath());
        member.setIntroduction(updateProfileRequest.getIntroduction());

        updateMemberSkills(member, updateProfileRequest);
    }

    public MemberResponse getMemberDetails() {
        Member member = getAuthenticatedMember();

        List<Long> memberSkillIdList = member.getMemberSkills().stream()
                .map(memberSkill -> memberSkill.getId().getSkillId())
                .collect(Collectors.toList());

        return buildMemberResponse(member, memberSkillIdList);
    }

    private void checkDuplMembers(CreateMemberRequest memberRequest) {
        checkDuplEmailAndProvider(memberRequest);
        checkDuplNickname(memberRequest);
    }

    private void checkDuplEmailAndProvider(CreateMemberRequest memberRequest) {
        memberRepository.findByEmailAndProvider(memberRequest.getEmail(), memberRequest.getProvider())
                .ifPresent(member -> {
                    throw new DuplEmailException(409, "이미 존재하는 이메일입니다. 다른 이메일을 사용해주세요.");}
                );
    }

    private void checkDuplNickname(CreateMemberRequest memberRequest) {
        memberRepository.findByNickname(memberRequest.getNickname())
                .ifPresent(member -> {
                    throw new DuplNicknameException(409, "이미 존재하는 닉네임입니다. 다른 닉네임을 사용해주세요.");}
                );
    }

    // TODO: DB 조회 -> 캐싱
    private List<Skill> fetchValidSkills(List<Long> skillIds) {
        if (skillIds.isEmpty()) {
            return null;
        }
        List<Skill> skills = skillRepository.findSkillsByIds(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new SkillsNotValidException(404, "스킬 데이터를 찾을 수 없습니다.");
        }

        return skills;
    }

    public Occupation validateOccupation(Long occupationId) {
        return occupationRepository.findById(occupationId).orElseThrow(
                () -> new DataNotFoundException()
        );
    }

    private UploadImageResponse uploadImageIfNeeded(UploadImageResponse defaultProfileImagePath, MultipartFile image) throws IOException {
        UploadImageResponse imageResponse = defaultProfileImagePath;

        if (!image.isEmpty()) {
            imageResponse = s3Service.uploadImage(image, "member");
        }
        return imageResponse;
    }

    private Member buildAndSaveMember(CreateMemberRequest memberRequest, Occupation occupation, UploadImageResponse imageResponse, String encodedPassword) {
        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(encodedPassword)
                .provider(memberRequest.getProvider())
                .roles(Role.valueOf("ROLE_USER"))
                .nickname(memberRequest.getNickname())
                .introduction(memberRequest.getIntroduction())
                .profileImageUrl(imageResponse.getImagePath())
                .occupation(occupation)
                .build();
        memberRepository.save(member);
        return member;
    }

    private void createMemberSkill(List<Skill> skills, Member member) {
        List<MemberSkill> memberSkills = skills.stream()
                .map(skill -> {
                    MemberSkillId memberSkillId = new MemberSkillId(member.getId(), skill.getId());

                    return MemberSkill.builder()
                            .id(memberSkillId)
                            .member(member)
                            .skill(skill)
                            .build();
                })
                .collect(Collectors.toList());

        memberSkillRepository.bulkInsert(memberSkills);
    }

    public void updateMemberSkills(Member member, UpdateProfileRequest updateMemberRequest) {
        List<Long> currentSkillIds = getSkillIdListFromMember(member);
        List<Long> newSkillIds = updateMemberRequest.getSkillIds();

        List<Long> skillsToAdd = newSkillIds.stream()
                .filter(skillId -> !currentSkillIds.contains(skillId))
                .collect(Collectors.toList());

        addMemberSkills(member, skillsToAdd);

        List<Long> skillsToRemove = currentSkillIds.stream()
                .filter(skillId -> !newSkillIds.contains(skillId))
                .collect(Collectors.toList());

        removeMemberSkills(member.getId(), skillsToRemove);
    }

    private List<Long> getSkillIdListFromMember(Member member) {
        return member.getMemberSkills().stream()
                .map(memberSkill -> memberSkill.getSkill().getId())
                .collect(Collectors.toList());
    }

    private void addMemberSkills(Member member, List<Long> skillIdsToAdd) {
        if (!skillIdsToAdd.isEmpty()) {
            List<Skill> skillsToAdd = skillRepository.findSkillsByIds(skillIdsToAdd);

            List<MemberSkill> newMemberSkills = skillsToAdd.stream()
                    .map(skill -> new MemberSkill(
                            new MemberSkillId(member.getId(), skill.getId()), member, skill)
                    )
                    .collect(Collectors.toList());

            memberSkillRepository.bulkInsert(newMemberSkills);
        }
    }

    private void removeMemberSkills(Long memberId, List<Long> skillIdsToRemove) {
        if (!skillIdsToRemove.isEmpty()) {
            memberSkillRepository.deleteAllByMemberIdAndSkillIdIn(memberId, skillIdsToRemove);
        }
    }

    private MemberResponse buildMemberResponse(Member member, List<Long> SkillIds) {
        return MemberResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .imageUrl(member.getProfileImageUrl())
                .introduction(member.getIntroduction())
                .skillIds(SkillIds)
                .occupation(member.getOccupation().getOccupation())
                .build();
    }

    private Claims checkTemporaryTokenForPasswordReset(String temporaryToken) {
        try {
            Claims claims = AccessTokenUtil.getClaimsFromAccessToken(temporaryToken);

            if (!claims.get("purpose").equals("password-reset")) {
                throw new AccessTokenInvalidException(403, "토큰이 올바르지 않습니다.");
            }
            return claims;
        } catch (Exception e) {
            throw new AccessTokenInvalidException(403, "토큰이 올바르지 않습니다.");
        }
    }

    private EmailVerification checkEmailVerification(ResetPasswordRequest passwordRequest) {
        EmailVerification emailVerification = emailVerificationRepository.findTopByReceiptEmailOrderByCreatedAtDesc(
                passwordRequest.getEmail()
        ).orElseThrow(() -> new EmailVerificationInvalidException(404, "해당 데이터를 찾을 수 없습니다."));

        LocalDateTime current = LocalDateTime.now();

        if (emailVerification.getExpireAt().isBefore(current) || !emailVerification.isCheckStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return emailVerification;
    }

    private Member getAuthenticatedMember() {
        Long memberId = SecurityContextHolderUtil.getMemberId();
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(404, "해당하는 사용자를 찾을 수 없습니다."));
    }
}