package org.devridge.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.Role;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.devridge.api.domain.emailverification.repository.EmailVerificationRepository;
import org.devridge.api.domain.member.dto.request.*;
import org.devridge.api.domain.member.dto.response.MemberResponse;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.entity.Occupation;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.member.repository.OccupationRepository;
import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.domain.skill.entity.key.MemberSkillId;
import org.devridge.api.domain.skill.repository.MemberSkillRepository;
import org.devridge.api.domain.skill.repository.SkillRepository;
import org.devridge.api.exception.email.EmailVerificationInvalidException;
import org.devridge.api.exception.member.*;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.devridge.common.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
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
    private final OccupationRepository occupationRepository;

    @Transactional
    public Long createMember(CreateMemberRequest memberRequest){
        checkDuplEmail(memberRequest);
        checkDuplNickname(memberRequest);

        Occupation occupation = areOccupationValid(memberRequest.getOccupationId());

        String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());
        Member member = buildAndSaveMember(memberRequest, occupation, encodedPassword);

        List<Long> skillIds = memberRequest.getSkillIds();

        if (!skillIds.isEmpty()) {
            List<Skill> skills = areSkillsValid(skillIds);
            createMemberSkill(skills, member);
        }

        return member.getId();
    }

    private Member buildAndSaveMember(CreateMemberRequest memberRequest, Occupation occupation, String encodedPassword) {
        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(encodedPassword)
                .provider(memberRequest.getProvider())
                .roles(Role.valueOf("ROLE_USER"))
                .nickname(memberRequest.getNickname())
                .introduction(memberRequest.getIntroduction())
                .occupation(occupation)
                .profileImageUrl(memberRequest.getProfileImageUrl())
                .build();

        return memberRepository.save(member);
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

    @Transactional
    public void deleteMember(DeleteMemberRequest memberRequest) {
        Member member = getAuthenticatedMember();

        if (!passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        memberRepository.delete(member);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest passwordRequest) {
        EmailVerification emailVerification = emailVerificationRepository.findTopByReceiptEmailOrderByCreatedAtDesc(
                passwordRequest.getEmail()
        ).orElseThrow(() -> new EmailVerificationInvalidException());

        LocalDateTime current = LocalDateTime.now();

        if (emailVerification.getExpireAt().isBefore(current) || !emailVerification.isCheckStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Member member = memberRepository.findByEmailAndProvider(passwordRequest.getEmail(), "normal").orElseThrow(
                () -> new MemberNotFoundException()
        );

        String encodedPassword = passwordEncoder.encode(passwordRequest.getPassword());
        member.changePassword(encodedPassword);
    }

    @Transactional
    public void updatePassword(ChangePasswordRequest passwordRequest) {
        Member member = getAuthenticatedMember();

        String encodedPassword = passwordEncoder.encode(passwordRequest.getPassword());
        member.changePassword(encodedPassword);
    }

    @Transactional
    public void updateMember(UpdateMemberProfileRequest updateMemberRequest) {
        Member member = getAuthenticatedMember();

        member.setProfileImageUrl(updateMemberRequest.getProfileImageUrl());
        member.setIntroduction(updateMemberRequest.getIntroduction());

        updateMemberSkills(member, updateMemberRequest);
    }

    public MemberResponse getMemberDetails() {
        Member member = getAuthenticatedMember();

        List<Long> memberSkillIdList = member.getMemberSkills().stream()
                .map(memberSkill -> memberSkill.getId().getSkillId())
                .collect(Collectors.toList());

        return buildMemberResponse(member, memberSkillIdList);
    }

    // TODO: DB 조회 -> 캐싱
    public List<Skill> areSkillsValid(List<Long> skillIds) {
        List<Skill> skills = skillRepository.findSkillsByIds(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new SkillsNotValidException();
        }
        return skills;
    }

    public Occupation areOccupationValid(Long occupationId) {
        return occupationRepository.findById(occupationId).orElseThrow(
                () -> new DataNotFoundException()
        );
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

    public void updateMemberSkills(Member member, UpdateMemberProfileRequest updateMemberRequest) {
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

    private Member getAuthenticatedMember() {
        Long memberId = SecurityContextHolderUtil.getMemberId();
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
    }
}