package proejct.spring.skeleton.constant.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("요청을 처리하는 도중에 문제가 발생하였습니다. 문제가 지속될 경우 고객센터로 문의바랍니다.");

    private String message;

    public static ErrorCode findByName(String name) {
        return Arrays.stream(ErrorCode.values())
                .filter(e -> e.name().equals(name))
                .findFirst()
                .orElse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}