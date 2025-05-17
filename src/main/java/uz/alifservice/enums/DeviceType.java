package uz.alifservice.enums;

public enum DeviceType {
    ANDROID,
    IOS,
    WEB,
    UNKNOWN;

    public static DeviceType fromAppId(String appId) {
        if (appId == null || appId.isEmpty()) {
            return UNKNOWN;
        }
        String[] parts = appId.split("\\.");
        String lastPart = parts[parts.length - 1].toUpperCase();
        try {
            return DeviceType.valueOf(lastPart);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
