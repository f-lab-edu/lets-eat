package com.letseat.domain.member.repository;

import com.letseat.domain.member.domain.Member;
import com.letseat.global.auth.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원 리포지토리에서 로그인ID로 회원을 조회하여 Member 객체를 가져오기")
    void findByLoginId() {
        //given
        Member member = createMember("inho5389", "1234", "정인호", "test@google.com", "01012345678");
        memberRepository.save(member);
        //when
        Optional<Member> savedMember = memberRepository.findByLoginId("inho5389");
        //then
        assertThat(member.getName()).isEqualTo(savedMember.get().getName());
        assertThat(member.getLoginId()).isEqualTo(savedMember.get().getLoginId());
    }
    @Test
    @DisplayName("회원 리포지토리에서 로그인ID로 회원을 조회했지만 존재하지 않을때 값을 꺼내면 NoSuchElementException예외가 발생한다.")
    void findByLoginIdNotExist() {
        //given
        //when
        Optional<Member> savedMember = memberRepository.findByLoginId("inho5389");
        //then
        assertThatThrownBy(()->savedMember.get())
                .isInstanceOf(NoSuchElementException.class);
    }



        private static Member createMember(String loginId, String password, String name, String email, String phone) {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phone(phone)
                .role(Role.ROLE_MEMBER)
                .build();
    }

}