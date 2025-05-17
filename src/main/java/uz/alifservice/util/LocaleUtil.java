package uz.alifservice.util;

import org.springframework.context.i18n.LocaleContextHolder;
import uz.alifservice.enums.AppLanguage;

import java.util.Locale;

public class LocaleUtil {

    public static AppLanguage getAppLanguage() {
        Locale locale = LocaleContextHolder.getLocale();
        return AppLanguage.valueOf(locale.getLanguage().toUpperCase());
    }
}
