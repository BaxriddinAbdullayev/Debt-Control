package uz.alifservice.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.alifservice.config.app.AppLanguageContext;
import uz.alifservice.enums.AppLanguage;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ResourceBundleService {

    private final ResourceBundleMessageSource bundleMessage;

    public String getMessage(String code, AppLanguage lang) {
        return bundleMessage.getMessage(code, null, new Locale(lang.name()));
    }

    public String getMessage(String code) {
        AppLanguage lang = AppLanguageContext.get();
        return bundleMessage.getMessage(code, null, new Locale(lang.name()));
    }

    public String getSuccessCrudMessage(String operation) {
        AppLanguage lang = AppLanguageContext.get();
        return getMessage("operation.success." + operation, lang);
    }
}
