package proejct.spring.skeleton.exception;

import lombok.Getter;
import proejct.spring.skeleton.constant.common.ErrorCode;

@Getter
public class InvalidAuthenticationException extends RuntimeException {
    private ErrorCode errorCode;

    public InvalidAuthenticationException(String message) {
        super(message);
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public InvalidAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationException(Throwable cause) {
        super(cause);
    }

    public InvalidAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
