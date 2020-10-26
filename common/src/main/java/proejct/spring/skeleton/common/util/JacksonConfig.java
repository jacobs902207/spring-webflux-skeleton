package proejct.spring.skeleton.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Configuration
public class JacksonConfig {
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String OBJECT_MAPPER_NAME = "COMMON_OBJECT_MAPPER";

    @Primary
    @Bean(OBJECT_MAPPER_NAME)
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.registerModules(createJavaTimeModule(DEFAULT_DATE_TIME_FORMATTER), createBigDecimalModule());

        return mapper;
    }

    private static JavaTimeModule createJavaTimeModule(final DateTimeFormatter formatter) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.truncatedTo(ChronoUnit.NANOS).format(formatter));
            }
        });
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return LocalDateTime.parse(p.getValueAsString(), formatter);
            }
        });

        return javaTimeModule;
    }

    private static SimpleModule createBigDecimalModule() {
        SimpleModule bigDecimalModule = new SimpleModule();

        bigDecimalModule.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.stripTrailingZeros().toPlainString());
            }
        });

        return bigDecimalModule;
    }
}