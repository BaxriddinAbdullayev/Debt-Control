package uz.alifservice.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.alifservice.config.jackson.deserializer.ZonedDateTimeDeserializer;
import uz.alifservice.config.jackson.serializer.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule zoneDateTimeModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        return module;
    }
}
