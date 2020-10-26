package project.spring.skeleton.data.rdb.config.encrypt.converter;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter
@Component
@Configurable
public class StringAES256CryptConverter implements AttributeConverter<String, String> {
//    private static Crypt crypt;
//
//    @Autowired
//    public void init(@Qualifier("AES256Crypt") Crypt aes256Crypt) {
//        StringAES256CryptConverter.crypt = aes256Crypt;
//    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return Optional.ofNullable(attribute)
//                .map(crypt::encrypt)
                .orElse(Strings.EMPTY);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData)
//                .map(crypt::decrypt)
                .orElse(Strings.EMPTY);
    }
}