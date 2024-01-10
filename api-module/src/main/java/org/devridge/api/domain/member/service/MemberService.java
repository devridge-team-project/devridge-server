package org.devridge.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.SkillConstants;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.domain.skill.entity.MemberSkillId;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.domain.skill.repository.MemberSkillRepository;
import org.devridge.api.domain.skill.repository.SkillRepository;
import org.devridge.api.exception.member.DuplEmailException;
import org.devridge.api.exception.member.PasswordNotMatchException;
import org.devridge.api.exception.member.SkillsNotValidException;
import org.devridge.api.security.constant.SecurityConstant;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        System.out.println("=== createMember() ===");
        checkDuplMember(memberRequest);
        passwordChecker.checkWeakPassword(memberRequest.getPassword());

        String[] skills = getSkills(memberRequest.getSkills());

        if (!areSkillsValid(skills)) {
            throw new SkillsNotValidException();
        }

        Member member = createNormalMember(memberRequest);
        createMemberSkill(skills, member);
    }

    public void createMemberSkill(String[] skills, Member member) {
        System.out.println("== createMemberSkill() ==");

        /**
         * 성능 최적화 하기!! (SQL query)
        * */
        for (String userSkill : skills) {
            Skill skill = skillRepository.findBySkill(userSkill).get();
            MemberSkillId memberSkillId = new MemberSkillId(member.getId(), skill.getId());

            MemberSkill memberSkill = MemberSkill.builder()
                    .id(memberSkillId)
                    .member(member)
                    .skill(skill)
                    .build();
            memberSkillRepository.save(memberSkill);
        }
    }

    private void checkDuplMember(CreateMemberRequest reqDto) {
        final Optional<Member> user = memberRepository.findByEmailAndProvider(reqDto.getEmail(), reqDto.getProvider());

        if (user.isPresent()) {
            throw new DuplEmailException("[ERROR] 이미 존재하는 계정입니다.");
        }
    }

    private Member createNormalMember(CreateMemberRequest reqDto) {
        System.out.println("=== createNormalMember() === ");
        String encodedPassword = passwordEncoder.encode(reqDto.getPassword());

        Member member = Member.builder()
                .email(reqDto.getEmail())
                .password(encodedPassword)
                .provider("normal")
                .roles("ROLE_" + SecurityConstant.USER_ROLE)
                .nickname(reqDto.getNickname())
                .build();

        return memberRepository.save(member);
    }

    public void deleteMember(DeleteMemberRequest reqDto) {
        Member member = SecurityContextHolderUtil.getMember();

        if (!passwordEncoder.matches(reqDto.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        member.softDelete();
    }

    public boolean areSkillsValid(String[] skills) {
        Set<String> userSkillSet = new HashSet<>();

        for (String skill : skills) {
            userSkillSet.add(skill);
        }

        return SkillConstants.SKILL_SET.containsAll(userSkillSet);
    }

    public String[] getSkills(String skills){
        String[] skillSet = Arrays.stream(skills.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toArray(String[]::new);
        return skillSet;
    }

}
