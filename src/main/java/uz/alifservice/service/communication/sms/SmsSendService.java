package uz.alifservice.service.communication.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.alifservice.domain.communication.sms.SmsProviderTokenHolder;
import uz.alifservice.dto.communication.sms.SmsAuthDTO;
import uz.alifservice.dto.communication.sms.SmsAuthResponseDTO;
import uz.alifservice.dto.communication.sms.SmsRequestDTO;
import uz.alifservice.dto.communication.sms.SmsSendResponseDTO;
import uz.alifservice.enums.AppLanguage;
import uz.alifservice.enums.SmsType;
import uz.alifservice.exps.AppBadException;
import uz.alifservice.repository.communication.sms.SmsProviderTokenHolderRepository;
import uz.alifservice.service.message.ResourceBundleService;
import uz.alifservice.util.RandomUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsSendService {

    private final RestTemplate restTemplate;
    private final SmsProviderTokenHolderRepository smsProviderTokenHolderRepository;
    private final SmsHistoryService smsHistoryService;
    private final ResourceBundleService bundleService;

    @Value("${eskiz.url}")
    private String smsURL;
    @Value("${eskiz.login}")
    private String accountLogin;
    @Value("${eskiz.password}")
    private String accountPassword;
    @Value("${sms.limit}")
    private Integer smsLimit;

    public void sendRegistrationSms(String phoneNumber, AppLanguage lang) {
        String code = RandomUtil.getRandomSmsCode();
        System.out.println(code);
        // test uchun ishlatilyapdi
        String message = bundleService.getMessage("sms.registration.confirm.code", lang);
//        String message = "Ro'yxatdan o'tish uchun tasdiqlash codi (code) : %s";
//        message = String.format(message,code);
        sendSms(phoneNumber, message, code, SmsType.REGISTRATION);
    }

    public void sendResetPasswordSms(String phoneNumber, AppLanguage lang) {
        String code = RandomUtil.getRandomSmsCode();
        System.out.println(code);
        // test uchun ishlatilyapdi
        String message = bundleService.getMessage("sms.reset.password.confirm", lang);
//        String message = "Ro'yxatdan o'tish uchun tasdiqlash codi (code) : %s";
//        message = String.format(message,code);
        sendSms(phoneNumber, message, code, SmsType.RESET_PASSWORD);
    }

    public void sendUsernameChangeConfirmSms(String phoneNumber, AppLanguage lang) {
        String code = RandomUtil.getRandomSmsCode();
        System.out.println(code);
        // test uchun ishlatilyapdi
        String message = bundleService.getMessage("sms.change.username.confirm", lang);
//        String message = "Ro'yxatdan o'tish uchun tasdiqlash codi (code) : %s";
//        message = String.format(message,code);
        sendSms(phoneNumber, message, code, SmsType.CHANGE_USERNAME_CONFIRM);
    }

    private SmsSendResponseDTO sendSms(String phoneNumber, String message, String code, SmsType smsType) {
        // check
        Long count = smsHistoryService.getSmsCount(phoneNumber);
        if (count >= smsLimit) {
            System.out.println("---- Sms Limit Reached. Phone : " + phoneNumber);
            throw new AppBadException("Sms limit reached");
        }

//        SmsSendResponseDTO result = sendSms(phoneNumber, message);
        smsHistoryService.create(phoneNumber, message, code, smsType);
        return null;
    }

    private SmsSendResponseDTO sendSms(String phoneNumber, String message) {
        // get Token
        String token = getToken();
        // send sms
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        SmsRequestDTO body = new SmsRequestDTO();
        body.setMobile_phone(phoneNumber);
        body.setMessage(message);
        body.setFrom("4546");

        HttpEntity<SmsRequestDTO> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<SmsSendResponseDTO> response = restTemplate.exchange(
                    smsURL + "/message/sms/send",
                    HttpMethod.POST,
                    entity,
                    SmsSendResponseDTO.class
            );
            return response.getBody();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getToken() {
        Optional<SmsProviderTokenHolder> optional = smsProviderTokenHolderRepository.findTop1By();
        if (optional.isEmpty()) { // if token not exists
            String token = getTokenFromProvider();
            SmsProviderTokenHolder entity = new SmsProviderTokenHolder();
            entity.setToken(token);
            entity.setExpiredDate(LocalDateTime.now().plusMonths(1));
            smsProviderTokenHolderRepository.save(entity);
            return token;
        }

        // if exists check it
        SmsProviderTokenHolder entity = optional.get();
        if (LocalDateTime.now().isBefore(entity.getExpiredDate())) { // if not expired
            return entity.getToken();
        }
        // get new token and update
        String token = getTokenFromProvider();
        entity.setToken(token);
        entity.setExpiredDate(LocalDateTime.now().plusMonths(1));
        smsProviderTokenHolderRepository.save(entity);
        return token;
    }

    private String getTokenFromProvider() {

        SmsAuthDTO smsAuthDTO = new SmsAuthDTO();
        smsAuthDTO.setEmail(accountLogin);
        smsAuthDTO.setPassword(accountPassword);

        try {
            System.out.println("------ SmsSender new Token was taken ------");
            SmsAuthResponseDTO response = restTemplate.postForObject(smsURL + "/auth/login", smsAuthDTO, SmsAuthResponseDTO.class);
            return response.getData().getToken();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
