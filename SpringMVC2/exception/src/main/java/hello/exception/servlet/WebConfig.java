package hello.exception.servlet;

import hello.exception.filter.LogFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<Filter> logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // 이 필터는 DispatcherType.REQUEST, DispatcherType.ERROR 타입을 경우에 호출된다. 디폴트는 DispatcherType.REQUEST
        return filterFilterRegistrationBean;
    }

}

//DispatcherType
//        REQUEST : 클라이언트 요청
//        ERROR : 오류 요청
//        FORWARD : MVC 에서 배웠던 서블릿에서 다른 서블릿이나 JSP 를 호출할 때
//        RequestDispatcher.forward(request, response);
//        INCLUDE : 서블릿에서 다른 서블릿이나 JSP 의 결과를 포함할 때
//        RequestDispatcher.include(request, response);
//        ASYNC : 서블릿 비동기 호출