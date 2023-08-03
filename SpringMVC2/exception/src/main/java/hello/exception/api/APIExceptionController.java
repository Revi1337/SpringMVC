package hello.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController @Slf4j
public class APIExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable String id) {
        if (id.equals("ex"))
            throw new RuntimeException("잘못된 사용자");
        if (id.equals("bad"))
            throw new IllegalArgumentException("잘못된 입력 값");
        return new MemberDto(id, "hello " + id);
    }

    @Data @AllArgsConstructor
    public static class MemberDto {
        private String memberId;
        private String name;
    }
}