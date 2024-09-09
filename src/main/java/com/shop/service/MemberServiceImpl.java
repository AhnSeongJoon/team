package com.shop.service;


import com.shop.dto.FindPwRequestDto;
import com.shop.dto.FindPwResponseDto;
import com.shop.entity.Member;
import com.shop.pservice.MemberService;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;



    // 회원 자동 생성
    @Override
    public Member autoRegister() {
        Member member = Member.builder()
                .name(UUID.randomUUID().toString())
                .email("smfqha26@naver.com")
                .address("서울특별시 서초구 역삼동")
                .build();

        return memberRepository.save(member);
    }

    @Override
    public String findPw(FindPwRequestDto request) throws Exception {
        return null;
    }
}