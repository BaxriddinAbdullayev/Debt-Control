package uz.alifservice.config.app;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import uz.alifservice.config.interceptor.DeviceInterceptor;

import java.util.Locale;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final DeviceInterceptor deviceInterceptor;

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(new Locale("UZ"));
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceInterceptor);
    }
}

