package org.devridge.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.Role;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.domain.skill.entity.key.MemberSkillId;
import org.devridge.api.domain.skill.repository.MemberSkillRepository;
import org.devridge.api.domain.skill.repository.SkillRepository;
import org.devridge.api.exception.member.DuplEmailException;
import org.devridge.api.exception.member.MemberNotFoundException;
import org.devridge.api.exception.member.PasswordNotMatchException;
import org.devridge.api.exception.member.SkillsNotValidException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final PasswordChecker passwordChecker;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void createMember(CreateMemberRequest memberRequest){
        checkDuplMember(memberRequest);
        passwordChecker.checkWeakPassword(memberRequest.getPassword());

        String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());

        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(encodedPassword)
                .provider("normal")
                .roles(Role.valueOf("ROLE_USER"))
                .nickname(memberRequest.getNickname())
                .build();

        memberRepository.save(member);

        List<Long> skillIds = memberRequest.getSkillIds();

        if (!skillIds.isEmpty()) {
            List skills = areSkillsValid(skillIds);
            createMemberSkill(skills, member);
        }
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

    private void checkDuplMember(CreateMemberRequest reqDto) {
        final Optional<Member> user = memberRepository.findByEmailAndProvider(reqDto.getEmail(), reqDto.getProvider());

        if (user.isPresent()) {
            throw new DuplEmailException("[ERROR] 이미 존재하는 계정입니다.");
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

    public List<Skill> areSkillsValid(List<Long> skillIds) {
        List<Skill> skills = skillRepository.findAllById(skillIds);

        if (skills.size() != skillIds.size()) {
            throw new SkillsNotValidException();
        }

        return skills;
    }
}
