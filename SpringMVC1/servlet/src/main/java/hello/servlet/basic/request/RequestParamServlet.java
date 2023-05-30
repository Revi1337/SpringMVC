package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    // GET 으로 ?username=dummy&age=123 을 보내나
    // POST 로 바디에 form data 로 username=dummy&age=123 을 보내나

    // key, value 형태는 똑같기때문에, 서버입장에서는 getParameter() 로 GET 파람, POST 바디의 파람 모두 받아줄 수 있다.
    // 추가적으로 POST 요청시에 바디에  username=dummy, 쿼리스트링의 파라미터에 username=user 둘 다 넣어도 이름이 같은 복수 파라미터로 조회된다.

    // Content-Type 은 Http Body 의 데이터 형식을 지정해주는 것이기때문에 GET 요청에는 존재하지 않는다.
    // GET 쿼리파람으로 보내면, 바디를 사용하지 않기 때문에 Content-Type: application/x-www-form-urlencoded; 가 없다.
    // POST 폼 형식으로 보내면 , Content-Type: application/x-www-form-urlencoded; 가 없다.
    // 당연한소리지만.. html form 으로 POST 요청을 보내면 브라우저에서 자동으로 헤더에 application/x-www-form-urlencoded 를 달아서 보내준다.

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회] - start");
        request.getParameterNames().asIterator()
                .forEachRemaining(param -> System.out.println(request.getParameter(param)));
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회] - start");
        System.out.println(request.getParameter("username"));
        System.out.println(request.getParameter("age"));
        System.out.println("[단일 파라미터 조회] - end");
        System.out.println();

        System.out.println("[이름이 같은 복수 파라미터 조회] - start");
        Arrays.stream(request.getParameterValues("username"))
                .forEach(value -> System.out.println("username : " + value));
        System.out.println("[이름이 같은 복수 파라미터 조회] - end");
        System.out.println();

        response.getWriter().println("ok");
    }
}
