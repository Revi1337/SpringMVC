package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller @Slf4j
public class RequestParamController {

    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     * GET, POST 둘다 getParameter() 로 꺼낼 수 있음.
     * 하지만 POST 요청 시, 동일한 파람이 Body 와 QueryString 에 존재하면 QueryString 이 우선권을 가져가게된다.
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(path = "/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    /**
     * @RequestParam 사용
     * - 파라미터 이름으로 바인딩 (기본적으로 @RequestParam 은 required=true 임.)
     * @ResponseBody 추가
     * - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
     * @param memberName
     * @param memberAge
     * @return
     */
    @RequestMapping("/request-param-v2")
    @ResponseBody
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {
        log.info("username={}, age={}", memberName, memberAge);

        return "ok";
    }

    /**
     * @RequestParam 사용 시 HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
     * @param username
     * @param age
     * @return
     */
    @RequestMapping("/request-param-v3")
    @ResponseBody
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam 사용 시 String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
     * 생략하면 @RequestParam(required=false) 로 동작함
     * @param username
     * @param age
     * @return
     */
    @RequestMapping("/request-param-v4")
    @ResponseBody
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam - 조심해야할 것은 @RequestParam(required = false) 를 사용할 떄 받는 변수가 Primitive 타입이면 null 로 넣어줄수가없어서, Wrapper 타입으로 선언해주어야 한다
     * 혹은 파라미터를 꼭 Primitive 타입으로 받고싶다면, default value 를 설정해주면 된다.
     *
     * @param username
     * @param age
     * @return
     */
    @RequestMapping("/request-param-required")
    @ResponseBody
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false, defaultValue = "1") int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam - 기본값 적용
     * required=true 여도 username= 를 넣어주면 null 이 아닌 빈문자열 "" 이기때문에 OK 가 되게된다. 따라서 defaultValue 를 설정해주어야 안전하다.
     * 이는 defaultValue 는 빈문자의 경우에도 기본값이 설정된다는 의미이다.
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "1") int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam Map, MultiValueMap - 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap 을 사용하자.
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     * @param paramMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"),
                paramMap.get("age"));

        return "ok";
    }

}
