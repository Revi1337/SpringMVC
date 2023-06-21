package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController @Slf4j
public class RequestHeaderController {
    // @Controller 에 사용 가능한 파라미터 목록
    // https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/arguments.html
    // @Controller 에 사용 가능한 응답 값 목록
    // https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types

    /**
     * HttpServletResponse
     * HttpMethod : HTTP 메서드를 조회한다. org.springframework.http.HttpMethod
     * Locale : Locale 정보를 조회한다.
     * @RequestHeader MultiValueMap<String, String> headerMap
     * 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
     * @RequestHeader("host") String host
     * 특정 HTTP 헤더를 조회한다.
     * 속성
     * 필수 값 여부: required
     * 기본 값 속성: defaultValue
     * @CookieValue(value = "myCookie", required = false) String cookie
     * 특정 쿠키를 조회한다.
     * 속성
     * 필수 값 여부: required
     * 기본 값: defaultValue
     * MultiValueMap
     * MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
     * HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
     * keyA=value1&keyA=value2
     */
    @RequestMapping(path = "/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie) {
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);

        return "ok";
    }
}
