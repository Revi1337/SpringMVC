package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController @Slf4j
public class RequestParamController {

    /**
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

}
