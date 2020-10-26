package proejct.spring.skeleton.common.constant.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_ARGUMENTS("요청 값을 다시 한번 확인해주세요."),
    NOT_FOUND("요청하신 데이터를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("요청을 처리하던 중에 문제가 발생하여 처리할 수 없습니다.");

    private String message;

    public static ErrorCode findByName(String name) {
        return Arrays.stream(ErrorCode.values())
                .filter(e -> e.name().equals(name))
                .findFirst()
                .orElse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}