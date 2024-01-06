package org.devridge.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.MemberConstant;
import org.devridge.api.constant.SkillConstants;
import org.devridge.api.domain.member.dto.request.CreateMemberRequest;
import org.devridge.api.domain.member.dto.request.DeleteMemberRequest;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.exception.member.DuplEmailException;
import org.devridge.api.exception.member.PasswordNotMatchException;
import org.devridge.api.exception.member.SkillsNotValidException;
import org.devridge.api.security.constant.SecurityConstant;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordChecker passwordChecker;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createMember(CreateMemberRequest reqDto){
        checkDuplMember(reqDto);
        passwordChecker.checkWeakPassword(reqDto.getPassword());

        Member member = createNormalMember(reqDto);

        memberRepository.save(member);
    }

    private void checkDuplMember(CreateMemberRequest reqDto) {
        final Optional<Member> user = memberRepository.findByEmailAndProvider(reqDto.getEmail(), reqDto.getProvider());

        if (user.isPresent()) {
            throw new DuplEmailException("[ERROR] 이미 존재하는 계정입니다.");
        }
    }

    private Member createNormalMember(CreateMemberRequest reqDto) {
        String encodedPassword = passwordEncoder.encode(reqDto.getPassword());

        Set<String> userSkills = new HashSet<>(Arrays.asList(reqDto.getSkillSet().split(",")));

        if (!areSkillsValid(userSkills)) {
            throw new SkillsNotValidException();
        }

        Member member = Member.builder()
                .email(reqDto.getEmail())
                .password(encodedPassword)
                .provider(MemberConstant.PROVIDER_NORMAL)
                .roles(SecurityConstant.USER_ROLE)
                .nickname(reqDto.getNickname())
                .build();

        return member;
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

    public boolean areSkillsValid(Set<String> userSkills) {
        return SkillConstants.SKILL_SET.containsAll(userSkills);
    }
}
