package org.devridge.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.Role;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.devridge.api.domain.emailverification.repository.EmailVerificationRepository;
import org.devridge.api.domain.member.dto.request.ChangePasswordRequest;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
import org.devridge.api.domain.member.dto.request.UpdateMemberProfileRequest;
import org.devridge.api.domain.member.dto.response.MemberResponse;
import org.devridge.api.domain.member.dto.response.UpdateMemberResponse;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.domain.skill.entity.key.MemberSkillId;
import org.devridge.api.domain.skill.repository.MemberSkillRepository;
import org.devridge.api.domain.skill.repository.SkillRepository;
import org.devridge.api.exception.email.EmailVerificationInvalidException;
import org.devridge.api.exception.member.*;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public Long createMember(CreateMemberRequest memberRequest){
        checkDuplEmail(memberRequest);
        checkDuplNickname(memberRequest);

        String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());
        Member member = buildAndSaveMember(memberRequest, encodedPassword);

        List<Long> skillIds = memberRequest.getSkillIds();

        if (!skillIds.isEmpty()) {
            List skills = areSkillsValid(skillIds);
            createMemberSkill(skills, member);
        }

        return member.getId();
    }

    private Member buildAndSaveMember(CreateMemberRequest memberRequest, String encodedPassword) {
        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(encodedPassword)
                .provider(memberRequest.getProvider())
                .roles(Role.valueOf("ROLE_USER"))
                .nickname(memberRequest.getNickname())
                .profileImageUrl(memberRequest.getProfileImageUrl())
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

        memberSkillRepository.saveAll(memberSkills);
    }

    private void checkDuplEmail(CreateMemberRequest memberRequest) {
        memberRepository.findByEmailAndProvider(
                memberRequest.getEmail(), memberRequest.getProvider()
        ).ifPresent(member -> {
            throw new DuplEmailException();
        });
    }

    private void checkDuplNickname(CreateMemberRequest memberRequest) {
        Optional<Member> member = memberRepository.findByNickname(memberRequest.getNickname());

        if (member.isPresent()) {
            throw new DuplNicknameException();
        }
    }

    @Transactional
    public void deleteMember(DeleteMemberRequest memberRequest) {
        Member member = SecurityContextHolderUtil.getMember();

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberNotFoundException());

        if (!passwordEncoder.matches(memberRequest.getPassword(), findMember.getPassword())) {
            throw new PasswordNotMatchException();
        }

        memberRepository.delete(findMember);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest passwordRequest) {
        Member member = memberRepository.findByEmailAndProvider(passwordRequest.getEmail(), "normal")
                .orElseThrow(() -> new MemberNotFoundException());

        EmailVerification emailVerification = emailVerificationRepository.findTopByReceiptEmailOrderByCreatedAtDesc(
                passwordRequest.getEmail()
        ).orElseThrow(() -> new EmailVerificationInvalidException());

        LocalDateTime current = LocalDateTime.now();

        if (emailVerification.getExpireAt().isBefore(current) || !emailVerification.isCheckStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(passwordRequest.getPassword());
        member.changePassword(encodedPassword);
    }

    // TODO: DB 조회 -> 캐싱
    public List<Skill> areSkillsValid(List<Long> skillIds) {
        List<Skill> skills = skillRepository.findAllById(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new SkillsNotValidException();
        }

        return skills;
    }

    @Transactional
    public UpdateMemberResponse updateMember(Long targetMemberId, UpdateMemberProfileRequest updateMemberRequest) {
        Long currentMemberId = SecurityContextHolderUtil.getMemberId();

        if (!targetMemberId.equals(currentMemberId)) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException());

        member.updateProfile(
                updateMemberRequest.getProfileImageUrl(),
                updateMemberRequest.getIntroduction()
        );
        updateMemberSkills(member, updateMemberRequest);

        MemberResponse memberResponse = buildMemberResponse(member);

        return new UpdateMemberResponse(memberResponse);
    }

    private void updateMemberSkills(Member member, UpdateMemberProfileRequest updateMemberRequest) {
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
            List<Skill> skillsToAdd = skillRepository.findAllById(skillIdsToAdd);
            List<MemberSkill> newMemberSkills = skillsToAdd.stream()
                    .map(skill -> new MemberSkill(new MemberSkillId(member.getId(), skill.getId()), member, skill))
                    .collect(Collectors.toList());
            memberSkillRepository.saveAll(newMemberSkills);
        }
    }

    private void removeMemberSkills(Long memberId, List<Long> skillIdsToRemove) {
        if (!skillIdsToRemove.isEmpty()) {
            memberSkillRepository.deleteAllByMemberIdAndSkillIdIn(memberId, skillIdsToRemove);
        }
    }

    private MemberResponse buildMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .imageUrl(member.getProfileImageUrl())
                .introduction(member.getIntroduction())
                .skillIds(getSkillIdListFromMember(member))
                .build();
    }
}
