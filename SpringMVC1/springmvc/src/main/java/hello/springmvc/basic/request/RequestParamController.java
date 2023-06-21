package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller @Slf4j
public class RequestParamController {

    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
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

    /**
     * @RequestParam 사용
     * - 파라미터 이름으로 바인딩 (기본적으로 @RequestParam 은 required=true 임.)
     * @ResponseBody 추가
     * - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
     * @param memberName
     * @param memberAge
     * @return
     */
    @RequestMapping("/request-param-v2")
    @ResponseBody
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {
        log.info("username={}, age={}", memberName, memberAge);

        return "ok";
    }

    /**
     * @RequestParam 사용 시 HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
     * @param username
     * @param age
     * @return
     */
    @RequestMapping("/request-param-v3")
    @ResponseBody
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam 사용 시 String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
     * 생략하면 @RequestParam(required=false) 로 동작함
     * @param username
     * @param age
     * @return
     */
    @RequestMapping("/request-param-v4")
    @ResponseBody
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam - 조심해야할 것은 @RequestParam(required = false) 를 사용할 떄 받는 변수가 Primitive 타입이면 null 로 넣어줄수가없어서, Wrapper 타입으로 선언해주어야 한다
     * 혹은 파라미터를 꼭 Primitive 타입으로 받고싶다면, default value 를 설정해주면 된다.
     *
     * @param username
     * @param age
     * @return
     */
    @RequestMapping("/request-param-required")
    @ResponseBody
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false, defaultValue = "1") int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam - 기본값 적용
     * required=true 여도 username= 를 넣어주면 null 이 아닌 빈문자열 "" 이기때문에 OK 가 되게된다. 따라서 defaultValue 를 설정해주어야 안전하다.
     * 이는 defaultValue 는 빈문자의 경우에도 기본값이 설정된다는 의미이다.
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "1") int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    /**
     * @RequestParam Map, MultiValueMap - 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap 을 사용하자.
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     * @param paramMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"),
                paramMap.get("age"));

        return "ok";
    }

    /**
     * @ModelAttribute 를 사용하기 전에는 직접 Getter Setter 로 값을 대입하고 뽑아오고해야한다.
     * 이 번거로운 과정을 자동화시켜주는 것이 @ModelAttribute 이다.
     *
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/model-attribute-before")
    public String beforeModelAttribute(@RequestParam String username, @RequestParam int age) {
        HelloData helloData = new HelloData();
        helloData.setUsername(username);
        helloData.setAge(age);

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * @ModelAttribute 의 사용 :
     * Spring MVC 는 @ModelAttribute 가 있으면 다음을 실행한다.
     * 1. HelloData 객체를 생성한다.
     * 2. 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter 를 호출해서 파라미터의 값을 입력(바인딩) 한다.
     * 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.
     * 따라서 Setter 는 꼭 필요하다.
     *
     * 또한, 기본적으로 HelloData 의 필드들은 required=false 로 동작한다.
     * 그리고, int 같이 primitive 타입에 대해서도 값이 `age=` 가 아닌, `age` 파라미터 자체가 없을 시, 0 으로 세팅해준다.
     * 조심해야 할것은 username= 처럼 빈값을 넣어주면, null 을 채워주지만, age= 처럼 int 에 빈값을 넣으면 Exception 이 터진다.
     *
     * @param helloData
     * @return
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * @ModelAttritube 는 생략해줄 수 있다. 이방법이 우리가 일반적으로 쓰는 방법이다.
     * @param helloData
     * @return
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

}
