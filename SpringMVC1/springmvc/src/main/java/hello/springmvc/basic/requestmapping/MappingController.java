package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    //  GET, HEAD, POST, PUT, PATCH, DELETE
    @RequestMapping(path = {"/hello-basic", "/hello-go"}, method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mapping-get-v1");
        return "OK";
    }

    /**
     * 편리한 축약 애노테이션 (코드보기)
     *
     * @GetMapping
     * @PostMapping
     * @PutMapping
     * @DeleteMapping
     * @PatchMapping
     */
    @GetMapping(path = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }

    @GetMapping(path = "/mapping/*/dummy")
    public String mappingGetV3() {
        log.info("mapping-get-v3");
        return "asd";
    }

    /**
     * PathVariable 사용
     * 변수명이 같으면 생략 가능
     * @PathVariable("userId") String userId -> @PathVariable userId
     */
    @GetMapping(path = "/mapping/{userId}")
    public String mappingPath(@PathVariable(name = "userId") String data) {
        log.info("mappingPath userId={}", data);
        return "pathVariable";
    }

    @GetMapping(path = "/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath2(@PathVariable String userId,
                               @PathVariable String orderId) {
        log.info("mappingPath2 userId={}, orderId={}", userId, orderId);
        return "pathVariable";
    }

    /**
     * 특정 파라미터 조건 매핑. (파라미터로 추가 매핑)
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good}"
     */
    @GetMapping(path = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(path = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * 미디어 타입 조건 매핑 - HTTP 요청 `Content-Type`, consume
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * consumes = "text/plain"
     * consumes = {"text/plain", "application/*"}
     * consumes = MediaType.TEXT_PLAIN_VALUE
     * MediaType.APPLICATION_JSON_VALUE
     * 조심해야할것을 Content-Type 을 headers 로 보내면 안된다. --> 스프링 내부에서 Content-Type 에 따른 로직이 달리지기 때문
     */
    @PostMapping(path = "/mapping-consume", consumes = {MediaType.APPLICATION_JSON_VALUE })
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * 미디어 타입 조건 매핑 - HTTP 요청 Accept 헤더와 맞아야 함, produce
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     * produces = "text/plain"
     * produces = {"text/plain", "application/*"}
     * produces = MediaType.TEXT_PLAIN_VALUE
     * produces = "text/plain;charset=UTF-8"
     */
    @PostMapping(path = "/mapping-produce", produces = {MediaType.TEXT_HTML_VALUE})
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}