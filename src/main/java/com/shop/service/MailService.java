package com.shop.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.Context;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender; // MailConfig @Bean 객체

    public final String epw = createKey();

    private MimeMessage createMessage(String to) throws Exception {

        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + epw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); // 보내는 대상
        message.setSubject("회원 가입 이메일 인증"); // 제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요.<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다</p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE: <strong>";
        msgg += epw + "</strong><div><br/>";
        msgg += "<div>";
        message.setText(msgg, "utf-8", "html"); // 내용
        message.setFrom(new InternetAddress("gory0609@naver.com", "고양이")); // properties 에 입력한 내용

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random r = new Random();

        for (int i = 0; i < 8; i++) {
            int index = r.nextInt(3);
            switch (index) {
                case 0:
                    key.append((char) ((int) r.nextInt(26) + 97)); //a~z
                    break;
                case 1:
                    key.append((char) ((int) r.nextInt(26) + 65)); //A~Z
                    break;
                case 2:
                    key.append(r.nextInt(10));// 0~9
                    break;

            }
        }
        return key.toString();
    }

    // 랜덤 인증 코드 전송
    public String sendSimpleMessage(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalStateException();
        }
        return epw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
    // 비밀번호 재설정 인증 코드 발송 메서드 추가


    public void sendEmail(String to, String subject, String body) {
            // 이메일 발송 로직 구현
            // 예를 들어, JavaMailSender를 사용하여 이메일을 보낼 수 있습니다.
    }

}


