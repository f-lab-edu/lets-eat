package com.letseat.global.auth;

import com.letseat.member.domain.Member;
import com.letseat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.info("===============PrincipalDetailService.loadUserByUsername====================");
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(()-> new UsernameNotFoundException(""));

        return new PrincipalDetails(member);
    }
}
