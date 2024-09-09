package com.shop.pservice;

import com.shop.dto.FindPwRequestDto;
import com.shop.entity.Member;

public interface MemberService {
    Member autoRegister();

    String findPw(FindPwRequestDto request) throws Exception;
}
