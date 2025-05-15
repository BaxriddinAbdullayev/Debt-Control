package uz.alifservice.config.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import uz.alifservice.enums.AppLanguage;

public class AppLanguageArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AppLanguage.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        String lang = webRequest.getHeader("Accept-Language");
        if (lang == null || lang.isEmpty()) {
            return AppLanguage.UZ;
        }
        try {
            return AppLanguage.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AppLanguage.UZ;
        }
    }
}
