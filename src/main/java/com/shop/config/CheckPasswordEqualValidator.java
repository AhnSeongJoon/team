package com.shop.config;

import com.shop.dto.MemberFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class CheckPasswordEqualValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberFormDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberFormDto dto = (MemberFormDto) target;

        if (!dto.getPassword().equals(dto.getPassword_confirm())) {
            errors.rejectValue("password_confirm", "비밀번호 일치 오류", "비밀번호가 일치하지 않습니다.");
        }
    }
}
