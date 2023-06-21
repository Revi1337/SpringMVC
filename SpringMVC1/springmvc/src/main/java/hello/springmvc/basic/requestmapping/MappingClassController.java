package hello.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.*;

/**
 * 회원 관리 API
 * 회원 목록 조회: GET /users
 * 회원 등록: POST /users
 * 회원 조회: GET /users/{userId}
 * 회원 수정: PATCH /users/{userId}
 * 회원 삭제: DELETE /users/{userId}
 */
@RestController @RequestMapping(path = "/mapping/users")
public class MappingClassController {

    // 중요한것. Accept 헤더가 text/html 이면 error 를 html 형식으로 내려준다. */* 이거나 application/json 이면 error 를  json 으로 내려주게된다.

    @GetMapping
    public String user() {
        return "get users";
    }

    @PostMapping
    public String addUser() {
        return "post user";
    }

    @GetMapping(path = "/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId = " + userId;
    }

    @PatchMapping(path = "/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId = " + userId;
    }

    @DeleteMapping(path = "/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId = " + userId;
    }

}
