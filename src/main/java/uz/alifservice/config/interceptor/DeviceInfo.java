package uz.alifservice.config.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.alifservice.enums.DeviceType;

@Getter
@Setter
@AllArgsConstructor
public class DeviceInfo {

    private DeviceType deviceType;
    private String appVersion;
}
