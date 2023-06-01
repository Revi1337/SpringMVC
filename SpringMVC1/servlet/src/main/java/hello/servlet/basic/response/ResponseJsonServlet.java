package hello.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        HelloData helloData = new HelloData();
        helloData.setUsername("kim");
        helloData.setAge(26);

        String jsonResponse = objectMapper.writeValueAsString(helloData);
        response.setContentType("application/json");

//        response.setCharacterEncoding("utf-8");
//        response.getWriter().println(jsonResponse);

        response.getOutputStream().println(jsonResponse);
    }
}
// 몰랐던 정보
// application/json 은 스펙상 utf-8 형식을 항상 사용하도록 정의되어 있음. 따라서 charset=utf-8 과 같은 추가 파라미터를 지원하지 않는다.
// 만약 추가하게되면 의미없는 파라미터만을 추가한 것이 된다.
// 하지만, response.getWriter().println() 을 사용하게 되면 자동으로 charset 을 설정하기 때문에, response.getOutputStream() 으로 출력하면 그런 문제가 없다.
// 근데 달아줘도 상관이없다.