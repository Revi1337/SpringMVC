package hello.login.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        // @Controller, @RequestMapping 을 활용한 핸들러매핑 을 사용할 시 : HandlerMethod
        // /resources/static 과 같은 정적 리소스가 호출되는 경우 : ResourceHttpRequestHandler

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;     // 호출할 컨트롤러의 메서드이 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}] [{}] [{}]", uuid, requestURI, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        // Dispatcher Servlet 의 doDispatch() 메서드의 adapter.handle() 메서드를 통해 HandlerMethod 를 실행켰을떄, Exception 이 터지면 interceptor 의 postHandle() 이 호출되지않음.
        // --> doDispatch() 메서드 내부의 adapter.handle() 메서드는 try catch 로 감싸져있고, postHandle() 이 실행되는 시점은 adapter.handle() 보다 밑에있기 떄문에
        //     핸들러 메서드가 실행되는 adapter.handle() 에서 Exception 이 터지면, 바로 doDispatch() 메서드 내부의 catch 문으로 이동하기 때문.
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        // View 까지 렌더링하고 호출됨.
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}] [{}] [{}]", logId, requestURI, handler);
        if (ex != null)
            log.info("EXCEPTION [{}]", ex);
    }
}
