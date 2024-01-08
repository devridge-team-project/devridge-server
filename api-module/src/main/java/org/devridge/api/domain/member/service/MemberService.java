package org.devridge.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.MemberConstant;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final MemberSkillRepository memberSkillRepository;
    private final PasswordChecker passwordChecker;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void createMember(CreateMemberRequest reqDto){
        checkDuplMember(reqDto);
        passwordChecker.checkWeakPassword(reqDto.getPassword());
        
        if(!areSkillsValid(reqDto.getSkills())){
            throw new SkillsNotValidException();
        }

        Member member = createNormalMember(reqDto);
        createMemberSkill(reqDto, member);
    }

    @Transactional
    public void createMemberSkill(CreateMemberRequest reqDto, Member member) {
        Set<String> userSkills = new HashSet<>(List.of(reqDto.getSkills()));

        for (String userSkill : userSkills) {
            Optional<Skill> bySkill = skillRepository.findBySkill(userSkill);
        }

        /**
         * 성능 최적화 하기!! (SQL query)
        * */
        for (String userSkill : userSkills) {
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
        System.out.println("=== createNormalMember() = createMember() ");
        String encodedPassword = passwordEncoder.encode(reqDto.getPassword());

        Member member = Member.builder()
                .email(reqDto.getEmail())
                .password(encodedPassword)
                .provider(MemberConstant.PROVIDER_NORMAL)
                .roles("ROLE_" + SecurityConstant.USER_ROLE)
                .nickname(reqDto.getNickname())
                .build();

        return memberRepository.save(member);
    }

    public void deleteMember(DeleteMemberRequest reqDto) {
        Member member = SecurityContextHolderUtil.getMember();

        if (passwordEncoder.matches(reqDto.getPassword(), member.getPassword())){
            memberRepository.delete(member);
        }
        else{
            throw new PasswordNotMatchException();
        }
    }

    public boolean areSkillsValid(String[] skills) {
        Set<String> userSkillSet = new HashSet<>();

        for (String skill : skills) {
            userSkillSet.add(skill);
        }

        return SkillConstants.SKILL_SET.containsAll(userSkillSet);
    }
}
