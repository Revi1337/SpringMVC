package hello.servlet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// TODO 2. /hello 경로로 오면 helloServlet 서블릿(싱글톤)의 service 메서드가 실행된다
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override // /hello 요청이 오면 WAS 가  HttpServletRequest, HttpServletResponse 를 만들어서 Servlet 에게 던져줌.
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);

        String username = request.getParameter("username");
        System.out.println("username = " + username);

        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().println("response" + username);
    }
}
