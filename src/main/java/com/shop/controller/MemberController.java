package com.shop.controller;

import com.shop.config.CheckPasswordEqualValidator;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MailService;
import com.shop.service.MemberService;
// import com.shop.service.PaymentServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    @Autowired
    private CheckPasswordEqualValidator passwordValidator;
//    private final PaymentServices paymentServices; (문자인증)
    private String conFirm = "";
    private boolean confirmCheck = false;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             Model model) {
        passwordValidator.validate(memberFormDto, bindingResult);
        // @Valid 붙은 객체를 검사해서 결과에 에러가 있으면 실행
        if (bindingResult.hasErrors()){
            return "member/memberForm"; // 다시 회원가입으로 돌려보낸다.
        }
        if (!confirmCheck){
            model.addAttribute("errorMessage","이메일 인증을 하세요.");
            return "member/memberForm";
        }
        try{
            // Member 객체 생성
            Member member = Member.createMember(memberFormDto, passwordEncoder);

            // 이메일 주소가 "gory0609@naver.com"인 경우 ROLE_ADMIN 역할 부여
            if (memberFormDto.getEmail().equals("gory0609@naver.com")) {
                member.addRole("ROLE_ADMIN");
            }

            // 데이터 베이스에 저장
            memberService.saveMember(member);
        }
        catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfirm(@PathVariable("email")String email)
            throws Exception {
        conFirm = mailService.sendSimpleMessage(email);
        return new ResponseEntity<String>("인증 메일을 보냈습니다.", HttpStatus.OK);
    }

    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code")String code)
            throws Exception {
        if (conFirm.equals(code)){
            confirmCheck =true;
            return new ResponseEntity<String>("인증 성공하였습니다.",HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증 코드를 올바르게 입력해주세요.",HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/mypage")
    public String myPage() {
        return "/member/mypage";  // resources/templates/mypage.html 을 반환
    }


    /*@GetMapping("/memberPhoneCheck")
    public @ResponseBody String memberPhoneCheck(@RequestParam(value="to") String to) throws CoolsmsException {

        return paymentServices.PhoneNumberCheck(to);
    }*/

    // 비밀번호 변경 요청을 처리하는 메서드
    @PostMapping("/mypage/changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmNewPassword") String confirmNewPassword,
                                 Model model) {
        // 현재 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.findByEmail(email);

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "member/changePasswordForm";
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
            return "member/changePasswordForm";
        }

        // 비밀번호 변경
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedNewPassword);
        memberService.updateMember(member);  // 변경된 비밀번호를 저장

        model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/";  // 성공적으로 변경되면 마이페이지로 이동
    }

    @PostMapping("/find-password")
    @ResponseBody
    public ResponseEntity<Map<String, String>> findPassword(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();
        try {
            memberService.sendTemporaryPassword(email);
            response.put("message", "임시 비밀번호가 이메일로 발송되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}


