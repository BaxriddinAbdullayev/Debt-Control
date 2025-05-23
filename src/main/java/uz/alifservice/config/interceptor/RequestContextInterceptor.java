package uz.alifservice.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uz.alifservice.enums.DeviceType;

@Component
public class RequestContextInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<RequestContextInfo> CONTEXT_INFO = new ThreadLocal<>();

    @Value("${app.default-time-zone}")
    private String defaultTimeZone;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String appId = request.getHeader("App-ID");
        String appVersion = request.getHeader("App-Version");
        String timezone = request.getHeader("X-Timezone");

        DeviceType deviceType = DeviceType.fromAppId(appId);
        String version = appVersion != null ? appVersion : "Unknown";

        if (timezone == null || timezone.trim().isEmpty()) {
            timezone = defaultTimeZone;
        }

        CONTEXT_INFO.set(new RequestContextInfo(deviceType, version, timezone));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CONTEXT_INFO.remove();
    }

    public static RequestContextInfo getContextInfo() {
        return CONTEXT_INFO.get();
    }
}
