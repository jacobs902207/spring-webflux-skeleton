package proejct.spring.skeleton.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import proejct.spring.skeleton.common.exception.SkeletonException;

import java.io.IOException;

import static proejct.spring.skeleton.common.util.JacksonConfig.OBJECT_MAPPER_NAME;

@Slf4j
@Component
@SuppressWarnings("unchecked")
public class Jackson<T> {
    private final ObjectMapper objectMapper;

    public Jackson(@Qualifier(OBJECT_MAPPER_NAME) ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public T toObject(Object obj, Class<?> typeClass) {
        try {
            return (T) objectMapper.readValue(obj.toString(), typeClass);
        } catch (IOException ex) {
            log.error("Jackson#toObject, {}, obj: {}", "JSON 파싱에 실패하였습니다.", obj.toString(), ex);

            throw new SkeletonException("데이터를 변환하는 도중에 문제가 발생하였습니다. 잠시 후 다시 시도해주세요.");
        }
    }
}