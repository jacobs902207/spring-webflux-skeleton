package proejct.spring.skeleton.common.exception;

import lombok.Getter;
import proejct.spring.skeleton.common.constant.common.ErrorCode;

@Getter
public class ApiClientException extends RuntimeException {
    private ErrorCode errorCode;

    public ApiClientException(String message) {
        super(message);
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public ApiClientException(Throwable cause) {
        super(cause);
    }
}
