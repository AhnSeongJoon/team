package com.shop.service;


import com.shop.entity.Member;
import com.shop.pservice.MemberService;
import com.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


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

}