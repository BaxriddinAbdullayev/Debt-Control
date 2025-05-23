package uz.alifservice.config.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public ZonedDateTimeDeserializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        String date = jsonParser.getText();
        if (date == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, FORMATTER);
        return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }
}
