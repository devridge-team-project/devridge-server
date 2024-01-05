package org.devridge.api.security.auth;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomMemberDetailsService.class);
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("=== CustomMemberDetailsService - loadUserByUsername() ====");

        return memberRepository.findByEmail(username)
                .map(CustomMemberDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + username));
    }
}
