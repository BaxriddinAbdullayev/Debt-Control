package uz.alifservice.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uz.alifservice.enums.DeviceType;

@Component
public class DeviceInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<DeviceInfo> DEVICE_INFO = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String appId = request.getHeader("App-ID");
        String appVersion = request.getHeader("App-Version");

        DeviceType deviceType = DeviceType.fromAppId(appId);
        String version = appVersion != null ? appVersion : "Unknown";

        DEVICE_INFO.set(new DeviceInfo(deviceType, version));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DEVICE_INFO.remove();
    }

    public static DeviceInfo getDeviceInfo() {
        return DEVICE_INFO.get();
    }
}
