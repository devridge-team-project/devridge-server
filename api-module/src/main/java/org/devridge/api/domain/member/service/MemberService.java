package org.devridge.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.Role;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.devridge.api.domain.emailverification.repository.EmailVerificationRepository;
import org.devridge.api.domain.member.dto.request.ChangePasswordRequest;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
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

        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(encodedPassword)
                .provider(memberRequest.getProvider())
                .roles(Role.valueOf("ROLE_USER"))
                .nickname(memberRequest.getNickname())
                .profileImageUrl(memberRequest.getProfileImageUrl())
                .build();

        memberRepository.save(member);

        List<Long> skillIds = memberRequest.getSkillIds();

        if (!skillIds.isEmpty()) {
            List skills = areSkillsValid(skillIds);
            createMemberSkill(skills, member);
        }

        return member.getId();
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

    private void checkDuplEmail(CreateMemberRequest reqDto) {
        final Optional<Member> member = memberRepository.findByEmailAndProvider(reqDto.getEmail(), reqDto.getProvider());

        if (member.isPresent()) {
            throw new DuplEmailException();
        }
    }

    private void checkDuplNickname(CreateMemberRequest memberRequest) {
        Optional<Member> member = memberRepository.findByNickname(memberRequest.getNickname());

        if (member.isPresent()) {
            throw new DuplNicknameException();
        }
    }

    @Transactional
    public void deleteMember(DeleteMemberRequest reqDto) {
        Member member = SecurityContextHolderUtil.getMember();

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberNotFoundException("member not found"));

        if (!passwordEncoder.matches(reqDto.getPassword(), findMember.getPassword())) {
            throw new PasswordNotMatchException();
        }

        memberRepository.delete(findMember);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest passwordRequest) {
        Member member = memberRepository.findByEmailAndProvider(passwordRequest.getEmail(), "normal")
                .orElseThrow(() -> new MemberNotFoundException("member not found"));

        EmailVerification emailVerification = emailVerificationRepository.findTopByReceiptEmailOrderByCreatedAtDesc(
                passwordRequest.getEmail()
        ).orElseThrow(() -> new EmailVerificationInvalidException());

        LocalDateTime current = LocalDateTime.now();

        if (emailVerification.getExpireAt().isBefore(current) || !emailVerification.isCheckStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증을 수행해주새요");
        }

        member.changePassword(passwordRequest.getPassword());
    }

    public List<Skill> areSkillsValid(List<Long> skillIds) {
        List<Skill> skills = skillRepository.findAllById(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new SkillsNotValidException();
        }

        return skills;
    }
}
