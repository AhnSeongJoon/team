package com.shop.config;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    // Optional 클래스는 아래와 같은 value에 값을 저장하기 때문에 값이 null이더라도 바로 nullpointError가 발생하지 않으며,
    // 클래스 이기 때문에 각종 매소드를 제공
    @Override
    public Optional<String> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId="";
        if (authentication != null){
            // 현재 로그인 한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
}
