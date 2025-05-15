package uz.alifservice.service.communication.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.alifservice.domain.communication.sms.SmsHistory;
import uz.alifservice.enums.SmsType;
import uz.alifservice.exps.AppBadException;
import uz.alifservice.repository.communication.sms.SmsHistoryRepository;
import uz.alifservice.service.message.ResourceBundleService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsHistoryService {

    private final SmsHistoryRepository smsHistoryRepository;
    private final ResourceBundleService bundleService;

    public void create(String phoneNumber, String message, String code, SmsType smsType) {
        SmsHistory entity = new SmsHistory();
        entity.setPhone(phoneNumber);
        entity.setMessage(message);
        entity.setCode(code);
        entity.setType(smsType);
        entity.setAttemptCount(0);
        smsHistoryRepository.save(entity);
    }

    public Long getSmsCount(String phone) {
        LocalDateTime now = LocalDateTime.now();
        return smsHistoryRepository.countByPhoneAndCreatedAtBetween(phone, now.minusMinutes(1), now);
    }

    public void check(String phoneNumber, String code) {
        Optional<SmsHistory> optional = smsHistoryRepository.findTop1ByPhoneOrderByCreatedAtDesc(phoneNumber);
        if (optional.isEmpty()) {
            throw new AppBadException(bundleService.getMessage("verification.failed"));
        }

        SmsHistory entity = optional.get();
        if (entity.getAttemptCount() >= 3) {
            throw new AppBadException(bundleService.getMessage("attempts.number.expired"));
        }

        if (!entity.getCode().equals(code)) {
            smsHistoryRepository.updateAttemptCount(entity.getId());
            throw new AppBadException(bundleService.getMessage("invalid.code"));
        }

        LocalDateTime expDate = entity.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .plusMinutes(2);

        if (LocalDateTime.now().isAfter(expDate)) {
            throw new AppBadException(bundleService.getMessage("code.timed.out"));
        }
    }
}
