package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExceptionHandler(IllegalArgumentException exception) {
        log.error("[exceptionHandler] ex", exception);
        return new ErrorResult("BAD", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception exception) {
        log.error("[exceptionHandler] ex", exception);
        ErrorResult errorResult = new ErrorResult("EX", "내부 오류");
        return errorResult;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException exception) {
        log.error("[exceptionHandler] ex", exception);
        ErrorResult errorResult = new ErrorResult("USER-EX", exception.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

}
