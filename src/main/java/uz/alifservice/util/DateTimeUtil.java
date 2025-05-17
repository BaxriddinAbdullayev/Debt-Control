package uz.alifservice.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    // Mahalliy vaqtni UTC ga aylantirish
    public static ZonedDateTime toUTC(ZonedDateTime dateTime) {
        return dateTime != null ? dateTime.withZoneSameInstant(ZoneId.of("UTC")) : null;
    }

    // UTC vaqtini foydalanuvchi vaqt mintaqasiga aylantirish
    public static ZonedDateTime toLocal(ZonedDateTime utcDateTime, String targetZoneId) {
        return utcDateTime != null ? utcDateTime.withZoneSameInstant(ZoneId.of(targetZoneId)) : null;
    }

//    // Mahalliy vaqtni UTC ga aylantirish
//    public static ZonedDateTime convertToUTC(ZonedDateTime localDateTime) {
//        return localDateTime.withZoneSameInstant(ZoneId.of("UTC"));
//    }
//
//    // UTC vaqtini mahalliy vaqt mintaqasiga aylantirish
//    public static ZonedDateTime convertToLocal(ZonedDateTime utcDateTime, String targetZoneId) {
//        return utcDateTime.withZoneSameInstant(ZoneId.of(targetZoneId));
//    }
//
//    // Vaqtni ISO 8601 formatida qaytarish
//    public static String formatDateTime(ZonedDateTime dateTime) {
//        return dateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
//    }
//
//    // String formatidagi vaqtni ZonedDateTime ga aylantirish
//    public static ZonedDateTime parseDateTime(String dateTimeString) {
//        return ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
//    }
}
