package hello.servlet.web.springmvc.old;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


@Component(value = "/springmvc/old-controller")
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}

// 어노테이션 기반 Controller 가 나오기전에 사용했던 컨트롤러 (HandlerMapping 을 따로 처리해야 함.)

// TODO 해당 컨트롤러가 호출되려면 아래의 2가지가 필요.
// 1. HandlerMapping(핸들러 매핑)
//      --> 스프링 Bean 의 이름을 URL 패턴으로 맞춘것.
//      --> HandlerMapping 에서 이 컨트롤러를 찾을 수 있어야 한다.
//      --> ex. 스프링 빈의 이름으로 핸들러를 찾을 수 있는 HandlerMapping 이 필요함.
// 2. HandlerAdapter(핸들러 어댑터)
//      --> 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요.
//      --> ex. Controller 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾고 실행해야 한다.
// TODO 스프링부트가 자동 등록하는 HandlerMapping 과 HandlerAdapter
// TODO HandlerMapping
// 0. RequestMappingHandlerMapping : 애노테이션 기반의 컨틀러인 @RequestMapping 에서 사용
// 1. BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다.
// TODO HandlerAdapter
// 0. RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping 에서 사용
// 1. HttpRequestHandlerAdapter : HttpRequestHandler 처리
// 2. SimpleControllerHandlerAdapter : Controller 인터페이스 (어노테이션 X, 과거에 사용) 처리
// TODO 정리
// 1. HandlerMapping 으로 Handler 조회
//      --> HandlerMapping 을 순서대로 실행해서, 핸들러를 찾는다.
//      --> 이번경우는 Bean 이름으로 Handler 를 찾아야하기때문에, 이름 그대로 Bean 이름으로 핸들러를 찾아주는 BeanNameUrlHandlerMapping 이 실행에 성공하고 핸들러인 `OldController` 를 반환한다.
// 2. HandlerAdapter 조회
//      --> HandlerAdapter 의 supports() 를 순서대로 호출
//      --> SimpleControllerHandlerAdapter 가 Controller 인터페이스를 support() true 하므로 대상이된다.
// 3. HandlerAdapter 의 실행
//      --> DispatcherServlet 이 조회한 SimpleControllerHandlerAdapter 를 실행하면서 핸들러 정보도 함께 넘겨준다.
//      --> SimpleControllerHandlerAdapter 는 핸들러인 OldController 를 내부에서 실행하고, 그 결과를 반환한다.
// -------최종-------
// OldController 를 실행하면서 사용된 객체는 다음과 같다.
// HandlerMapping = BeanNameUrlHandlerMapping
// HandlerAdapter = SimpleControllerHandlerAdapter