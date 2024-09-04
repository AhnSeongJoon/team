package com.shop.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto{
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 6, max = 10, message = "비밀번호는 6자이상, 10자 이하로 입력해주세요.")
    private String password;
    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password_confirm;
    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;
    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String  memberPhone;
    private String picture;

}
