package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor // final, @NonNull 변수에 붙으면 자동 주입(Autowired)을 해줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository; //자동 주입됨
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;






    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member); // 데이터베이스에 저장을 하라는 명령
    }
    private void validateDuplicateMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()) != null) {
            throw new IllegalStateException("이미 가입된 회원입니다."); // 예외 발생
        }
    }
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        //빌더패턴
        return org.springframework.security.core.userdetails.User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
    public Member updateMember(Member member){
        return memberRepository.save(member);
    }

    public void sendTemporaryPassword(String email) {
        Member member = memberRepository.findByEmail(email);

        if (member != null) {
            String tempPassword = generateTemporaryPassword();
            member.setPassword(tempPassword); // 비밀번호 설정 또는 업데이트
            memberRepository.save(member);

            mailService.sendEmail(email, "임시 비밀번호 발급", "임시 비밀번호는: " + tempPassword);
        } else {
            throw new RuntimeException("이메일에 해당하는 사용자를 찾을 수 없습니다.");
        }
    }

    private String generateTemporaryPassword() {
        // 임시 비밀번호 생성 로직 (예: UUID, 랜덤 문자열 등)
        return UUID.randomUUID().toString().substring(0, 8); // 예: 8자리 임시 비밀번호
    }

}
