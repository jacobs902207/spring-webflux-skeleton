package proejct.spring.skeleton.exception;

import lombok.Getter;
import proejct.spring.skeleton.constant.common.ErrorCode;

@Getter
public class InvalidArgumentsException extends RuntimeException {
    private ErrorCode errorCode;

    public InvalidArgumentsException(String message) {
        super(message);
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public InvalidArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentsException(Throwable cause) {
        super(cause);
    }

    public InvalidArgumentsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
