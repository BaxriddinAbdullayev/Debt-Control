package uz.alifservice.service.communication.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.alifservice.domain.communication.email.EmailHistory;
import uz.alifservice.enums.SmsType;
import uz.alifservice.exps.AppBadException;
import uz.alifservice.repository.communication.email.EmailHistoryRepository;
import uz.alifservice.service.message.ResourceBundleService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {

    private final EmailHistoryRepository emailHistoryRepository;
    private final ResourceBundleService bundleService;

    public void create(String email, String code, SmsType emailType) {
        EmailHistory entity = new EmailHistory();
        entity.setEmail(email);
        entity.setCode(code);
        entity.setEmailType(emailType);
        entity.setAttemptCount(0);
        emailHistoryRepository.save(entity);
    }

    public Long getEmailCount(String email) {
        LocalDateTime now = LocalDateTime.now();
        return emailHistoryRepository.countByEmailAndCreatedAtBetween(email, now.minusMinutes(1), now);
    }

    public void check(String email, String code) {
        // find last sms by phoneNumber
        Optional<EmailHistory> optional = emailHistoryRepository.findTop1ByEmailOrderByCreatedAtDesc(email);
        if (optional.isEmpty()) {
            throw new AppBadException(bundleService.getMessage("verification.failed"));
        }

        EmailHistory entity = optional.get();
        //Attempt count
        if (entity.getAttemptCount() >= 3) {
            throw new AppBadException(bundleService.getMessage("attempts.number.expired"));
        }
        // check code
        if (!entity.getCode().equals(code)) {
            emailHistoryRepository.updateAttemptCount(entity.getId()); // update attempt count ++
            throw new AppBadException(bundleService.getMessage("invalid.code"));
        }

        // check time
        LocalDateTime expDate = entity.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .plusMinutes(2);
        if (LocalDateTime.now().isAfter(expDate)) { // not valid
            throw new AppBadException(bundleService.getMessage("code.timed.out"));
        }
    }
}
