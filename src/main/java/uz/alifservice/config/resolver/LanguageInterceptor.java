package uz.alifservice.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uz.alifservice.config.app.AppLanguageContext;
import uz.alifservice.enums.AppLanguage;

@Component
public class LanguageInterceptor implements HandlerInterceptor {

    private static final String HEADER_LANG = "Accept-Language";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(HEADER_LANG);

        AppLanguage lang;
        try {
            lang = AppLanguage.valueOf(header != null ? header.toUpperCase() : "UZ");
        } catch (IllegalArgumentException e) {
            lang = AppLanguage.UZ;
        }

        AppLanguageContext.set(lang);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppLanguageContext.clear();
    }
}
