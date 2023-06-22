package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    // 정리
    // 스프링(서버)에서 응답 데이터를 만드는 방법은 크게 3가지.
    // 1. 정적 리소스
    //      예) 웹 브라우저에 정적인 HTML, css, js를 제공할 때는, 정적 리소스를 사용한다.
    // 2. 뷰 템플릿 사용
    //      예) 웹 브라우저에 동적인 HTML 을 제공할 때는 뷰 템플릿을 사용한다.
    // 3. HTTP 메시지 사용
    //      HTTP API 를 제공하는 경우에는 HTML 이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.
    //      @ResponseBody , HttpEntity 를 사용하면, 뷰 템플릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 응답 데이터를 출력할 수 있다.

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView modelAndView = new ModelAndView("response/hello")
                .addObject("data", "hello!");

        return modelAndView;
    }


    // String 을 반환하는 경우 - View or HTTP 메시지
    // @ResponseBody 가 없으면 response/hello 로 뷰 리졸버가 실행되어서 뷰를 찾고, 렌더링 한다.
    // @ResponseBody 가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 response/hello 라는 문자가 입력된다.
    // 여기서는 뷰의 논리 이름인 response/hello 를 반환하면 다음 경로의 뷰 템플릿이 렌더링 되는 것을 확인할 수 있다. 실행: templates/response/hello.html
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!!");

        return "response/hello";
    }

    // 굉장히 비추천하는 방법 (아무것도 리턴하지 않을떄 컨트롤러의 경로와 View 의 논리적 경로가 똑같으면  그냥 templates`/response/hello`.html 를 호출한다.)
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!!!");
    }

}


