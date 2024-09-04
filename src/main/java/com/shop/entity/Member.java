package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    private String memberPhone;

    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String name, String email, String address){
        this.name=name;
        this.email=email;
        this.address=address;
    }

    // 역할을 저장할 Set
    @Transient
    private Set<SimpleGrantedAuthority> authorities = new HashSet<>();

    // ROLE_ADMIN 을 추가하는 메서드
    public void addRole(String role) {
        authorities.add(new SimpleGrantedAuthority(role));
    }


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member. setMemberPhone(memberFormDto.getMemberPhone());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER); // 기본적으로 ADMIN 역할 설정 -> ADMIN 회원 가입 후 USER 로 바꿔놓음
        member.addRole("ROLE_USER"); // 추가적으로 ROLE_ADMIN 역할 부여
        member.setPicture(memberFormDto.getPicture());
        return member;
    }

    public static Member createSocialMember(String name, String email, String picture){
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setRole(Role.USER);
        member.addRole("ROLE_USER");
        member.setPicture(picture);

        return member;
    }

    public Member updateMember(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;

    }
}
