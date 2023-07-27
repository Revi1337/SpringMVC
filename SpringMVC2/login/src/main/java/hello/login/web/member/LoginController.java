package hello.login.web.member;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.login.LoginForm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletResponse httpServletResponse) {
        if (bindingResult.hasErrors())
            return "login/loginForm";
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //  쿠키에는 영속 쿠키와 세션 쿠키가 있다.
        //  영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
        //  세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지 (이름만 세션쿠키이지 세션과 관련없다.)
        //  쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));

        httpServletResponse.addCookie(idCookie);
        return "redirect:/";
    }
}

//        ResponseCookie idCookie = ResponseCookie.from("memberId", String.valueOf(loginMember.getId())).build()