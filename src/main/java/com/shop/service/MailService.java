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

import java.io.UnsupportedEncodingException;
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

        String msgg = "<div style='margin:20px;'>"
                + "<h1> 안녕하세요. </h1>"
                + "<p>아래 코드를 복사해 입력해주세요.<p>"
                + "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>"
                + "<div style='font-size:130%'>"
                + "CODE: <strong>" + epw + "</strong><div><br/>"
                + "</div>";
        message.setText(msgg, "utf-8", "html"); // 내용
        message.setFrom(new InternetAddress("gory0609@naver.com", "고양이")); // 발신자

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random r = new Random();

        for (int i = 0; i < 8; i++) {
            int index = r.nextInt(3);
            switch (index) {
                case 0:
                    key.append((char) (r.nextInt(26) + 97)); // a~z
                    break;
                case 1:
                    key.append((char) (r.nextInt(26) + 65)); // A~Z
                    break;
                case 2:
                    key.append(r.nextInt(10)); // 0~9
                    break;
            }
        }
        return key.toString();
    }

    // 인증 코드 전송
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

    // 비밀번호 재설정 이메일 전송
    public void sendPasswordResetEmail(String to, String newPassword) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        try {
            helper.setFrom(new InternetAddress("gory0609@naver.com", "고양이"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("잘못된 이메일 주소입니다.");
        }

        helper.setTo(to);
        helper.setSubject("비밀번호 재설정 안내");

        // 이메일 내용 설정
        String content = "<p>안녕하세요,</p>"
                + "<p>새 비밀번호는 <strong style='font-size: 2em;'>" + newPassword + "</strong> 입니다.</p>"
                + "<p>이 비밀번호로 로그인한 후 비밀번호를 변경하세요.</p>";
        helper.setText(content, true);

        emailSender.send(message);
    }
}
