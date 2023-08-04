package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController @Slf4j
public class APIExceptionV2Controller {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalExceptionHandler(IllegalArgumentException exception) {
//        log.error("[exceptionHandler] ex", exception);
//        return new ErrorResult("BAD", exception.getMessage());
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userExHandler(UserException exception) {
//        log.error("[exceptionHandler] ex", exception);
//        ErrorResult errorResult = new ErrorResult("USER-EX", exception.getMessage());
//        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception exception) {
//        log.error("[exceptionHandler] ex", exception);
//        ErrorResult errorResult = new ErrorResult("EX", "내부 오류");
//        return errorResult;
//    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable String id) {
        if (id.equals("ex"))
            throw new RuntimeException("잘못된 사용자");
        if (id.equals("bad"))
            throw new IllegalArgumentException("잘못된 입력 값");
        if (id.equals("user-ex"))
            throw new UserException("사용자 오류");
        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    public static class MemberDto {
        private String memberId;
        private String name;
    }
}
