package proejct.spring.skeleton.common.exception;

import lombok.Getter;
import proejct.spring.skeleton.common.constant.common.ErrorCode;

@Getter
public class NotFoundException extends RuntimeException {
    private ErrorCode errorCode;

    public NotFoundException(String message) {
        super(message);
        errorCode = ErrorCode.NOT_FOUND;
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
