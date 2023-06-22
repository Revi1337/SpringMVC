package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
@Slf4j @Controller
public class RequestBodyJsonController {

    // 결론
    // 1. 스프링에서 @RequestBody 를 사용할때 요청으로 넘어오는 Content-Type 에 따라 작동하는 HttpMessageConverter 가 달라진다.
    // 2. @RequestBody 는 Body 를 조금 더 편하게 읽어오는것일뿐 @RequestParam 와, @ModelAttribute 와 관련이 없다.

    // 3. 스프링에서 @ResponseBody 를 사용할때 Client 가 받아들일수 있는 응답 타입인 Accept 헤더에 따라 HttpMessageConverter 가 달라져 나가는 응답이 달라진다.

    // @RequestBody 요청 : JSON 요청 --> HTTP 메시지 컨버터 --> 객체
    // @ResponseBody 응답 : 객체 --> HTTP 메시지 컨버터 --> JSON 응답

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(path = "/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }


    /**
     * Body 를 직접 읽어 ObjectMapper 를 통해 객체로 변환 --> 하지만, 이렇게 직접 변환하는 과정이 있어 귀찮고 불편한다.
     *
     * @RequestBody
     * RequestBody 를 사용하면 HttpMessageConverter 사용된다. -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 모든 메서드에 @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @param messageBody
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping(path = "/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        log.info("messageBody = {}", messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }


    /**
     * 최종적인 방법. --> @RequestBody 에 직접 만든 객체를 바인딩. @RequestBody 를 사용함과 동시에 Content-Type 에 application/json 이 넘어오게되면
     * JSON 을 객체로 convert 해주는 MappingJackson2HttpMessageConverter 가 동작하여 Body 에 JSON 올것임을 알고 이를 읽어 Body 로 넘어온 JSON 을 바인딩하는 객체로 자동으로 변환해준다.
     * 따라서 content-type 이 application/json 인지 꼭! 확인해야 한다. 그래야 JSON 을 처리할 수 있는 HTTP 메시지 컨버터가 실행된다.
     * 여기서 알 수 있는 것은 @RequestBody 를 사용할때 요청으로 넘어오는 Content-Type 에 따라 작동하는 HttpMessageConverter 가 달라진다는 것이다.
     *
     * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버린다.)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type: application/json)
     *
     * @param helloData
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping(path = "/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping(path = "/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) throws IOException {
        HelloData helloData = httpEntity.getBody();
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type: application/json)
     *
     * @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용 (Accept: application/json)
     * @param helloData
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping(path = "/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) throws IOException {
        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());

        return helloData;
    }

}
