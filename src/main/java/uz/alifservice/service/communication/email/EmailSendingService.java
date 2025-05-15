package uz.alifservice.service.communication.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.alifservice.enums.SmsType;
import uz.alifservice.exps.AppBadException;
import uz.alifservice.service.message.ResourceBundleService;
import uz.alifservice.util.JwtUtil;
import uz.alifservice.util.RandomUtil;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailSendingService {

    private final JavaMailSender javaMailSender;
    private final EmailHistoryService emailHistoryService;
    private final ResourceBundleService bundleService;
    private final JwtUtil jwtUtil;

    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${email.limit}")
    private Integer emailLimit = 3;

    public void sendRegistrationEmail(String email, SmsType smsType) {
        String code = RandomUtil.getRandomSmsCode();
        String subject = "";
        String body = "";
        switch (smsType) {
            case SmsType.REGISTRATION -> {
                subject = bundleService.getMessage("email.subject.resitration");
                body = bundleService.getMessage("confirm.code.message") + " " + code;
            }
            case SmsType.RESET_PASSWORD -> {
                subject = bundleService.getMessage("email.subject.reset.password");
                body = bundleService.getMessage("reset.password.code.message") + " " + code;
            }
        }
        checkAndSendMimeEmail(email, subject, body, code, smsType);
    }

    private void checkAndSendMimeEmail(
            String email,
            String subject,
            String body,
            String code,
            SmsType smsType
    ) {
        // check
        Long count = emailHistoryService.getEmailCount(email);
        if (count >= emailLimit) {
            System.out.println(" ----- Email Limit Reached. Email: " + email);
            throw new AppBadException(bundleService.getMessage("email.limit.reached"));
        }
        // send
        sendMimeEmail(email, subject, body);
        // create
        emailHistoryService.create(email, code, smsType);
    }

    private void sendMimeEmail(String email, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAccount);
            helper.setTo(email);
            helper.setSubject(subject);

            // HTML formatda kontentni belgilash
            helper.setText(body, true);

            CompletableFuture.runAsync(() -> {
                javaMailSender.send(message);
            });
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendSimpleEmail(String email, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }

}
