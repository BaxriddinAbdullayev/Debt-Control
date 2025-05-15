package uz.alifservice.config.app;

import uz.alifservice.enums.AppLanguage;

public class AppLanguageContext {

    private static final ThreadLocal<AppLanguage> context = new ThreadLocal<>();

    public static void set(AppLanguage lang) {
        context.set(lang);
    }

    public static AppLanguage get() {
        AppLanguage lang = context.get();
        return lang != null ? lang : AppLanguage.UZ;
    }

    public static void clear() {
        context.remove();
    }
}
