package uz.alifservice.config.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.beans.factory.annotation.Value;
import uz.alifservice.config.interceptor.RequestContextInterceptor;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Value("${app.default-time-zone}")
    private String defaultTimeZone;

    public ZonedDateTimeSerializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public void serialize(ZonedDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (dateTime == null) {
            jsonGenerator.writeNull();
            return;
        }

        String targetZoneId = RequestContextInterceptor.getContextInfo() != null
                ? RequestContextInterceptor.getContextInfo().getTimezone()
                : defaultTimeZone;
        ZonedDateTime converted = dateTime.withZoneSameInstant(ZoneId.of(targetZoneId));
        jsonGenerator.writeString(converted.format(FORMATTER));
    }
}
