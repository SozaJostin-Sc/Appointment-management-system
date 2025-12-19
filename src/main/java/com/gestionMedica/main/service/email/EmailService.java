package com.gestionMedica.main.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        String backendURl = "http://localhost:8080/api/auth/reset-password";
        String fullUrl = backendURl + "?token="+ token;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject("PasswordReset");

        String instructions = "Para resetear tu password, realiza un POST a: \n" +
                "http://localhost:8080/api/auth/reset-password \n\n" +
                "Con el siguiente cuerpo JSON:\n" +
                "{\n" +
                "  \"token\": \"" + token + "\",\n" +
                "  \"newPassword\": \"tu_nueva_password\"\n" +
                "}";
        helper.setText(instructions, true);

        mailSender.send(message);
    }
}
