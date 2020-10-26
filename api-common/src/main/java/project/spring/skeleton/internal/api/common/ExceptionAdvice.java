package project.spring.skeleton.internal.api.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import proejct.spring.skeleton.common.constant.common.ErrorCode;
import proejct.spring.skeleton.common.exception.ApiClientException;
import proejct.spring.skeleton.common.exception.NotFoundException;
import proejct.spring.skeleton.common.exception.SkeletonException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseErrorResponse handleNotFoundException(NotFoundException ex) {
        log.error("{}", ex.toString(), ex);

        return BaseErrorResponse.error(ex.getErrorCode().name(), ex.getMessage());
    }

    @ExceptionHandler(ApiClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseErrorResponse handleApiClientException(ApiClientException ex) {
        log.error("{}", ex.toString(), ex);

        return BaseErrorResponse.error(ex.getErrorCode().name(), ex.getMessage());
    }

    @ExceptionHandler(SkeletonException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseErrorResponse handlePartnerException(SkeletonException ex) {
        log.error("{}", ex.toString(), ex);

        return BaseErrorResponse.fail(ex.getErrorCode().name(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseErrorResponse handleDefaultException(Exception ex) {
        log.error("{}", ex.toString(), ex);

        return BaseErrorResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}