package com.shop.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdatePasswordReq {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;

}
