package hello.springmvc.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Controller @Slf4j
public class RequestBodyStringController {

    @PostMapping(path = "/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }

    /**
     * @InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
     * @OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
     * @param inputStream
     * @param responseWriter
     * @throws IOException
     */
    @PostMapping(path = "/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        responseWriter.write("V2 ok");
    }


    /**
     * @HttpEntity: HTTP header, body 정보를 편리하게 조회
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * 응답에서도 HttpEntity 사용 가능
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * 중요하게 생각해야하는 것은 HttpEntity  는 요청 파라미터를 조회하는 기능인  @RequestParam, @ModelAttribute 들과 관계가 아예 없다는것이다.
     * @param httpEntity
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        String messageBody = httpEntity.getBody();

        log.info("messageBody={}", messageBody);

        return new HttpEntity<>("ok");
    }


    /**
     * @HttpEntity - HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공한다.
     * @RequestEntity - HttpMethod, url 정보가 추가, 요청에서 사용한다.
     * @ResponseEntity - HTTP 상태 코드 설정 가능, 응답에서 사용한다.
     *
     * Spring MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데, 이때 HTTP 메시지 컨버터(HttpMessageConverter )라는 기능을 사용한다.
     *
     * @param requestEntity
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/request-body-string-v3-1")
    public HttpEntity<String> requestBodyStringV3_1(RequestEntity<String> requestEntity) throws IOException {
        String messageBody = requestEntity.getBody();

        log.info("messageBody={}", messageBody);

        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }


    /**
     * @RequestBody
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X 와 관계가 없다.)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     * - 헤더정보가 필요하다면 HttpEntity 를 사용하거나, @RequestHeader 를 사용하면된다.
     *
     * @ResponseBody
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     * - 메시지 바디정보를 직접 반환하기때문에, Status Code 등을 반환하려면 ResponseEntity 를사용해야 한다.
     *
     * @param messageBody
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping(path = "/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {
        log.info("messageBody={}", messageBody);

        return "ok";
    }

//    정리
//    @RequestBody
//    - @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 참고로 헤더 정보가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
//    - 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam, @ModelAttribute 와는 전혀 관계가 없다.

//    요청 파라미터 vs HTTP 메시지 바디
//    - 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
//    - HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody, @ResponseBody
//    - @ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다. (물론 이 경우에도 view 를 사용하지 않는다.)
}

