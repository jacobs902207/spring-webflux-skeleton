package proejct.spring.skeleton.common.exception;

import lombok.Getter;
import proejct.spring.skeleton.common.constant.common.ErrorCode;

@Getter
public class SkeletonException extends RuntimeException {
    private ErrorCode errorCode;

    public SkeletonException(String message) {
        super(message);
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public SkeletonException(Throwable cause) {
        super(cause);
    }
}
