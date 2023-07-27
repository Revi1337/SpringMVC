package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
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

    //    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    // 쿠키를 만료시킬때는 쿠키값에 null 을 넣어주고, Max-Age 를 0 으로 설정해주면 된다.
    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    // V2 ================================================================================================================================

//    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletResponse httpServletResponse) {
        if (bindingResult.hasErrors())
            return "login/loginForm";
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        sessionManager.createSession(loginMember, httpServletResponse);

        return "redirect:/";
    }


//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest httpServletRequest) {
        sessionManager.expire(httpServletRequest);
        return "redirect:/";
    }

    // V3 ================================================================================================================================

    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors())
            return "login/loginForm";

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 세션이 있으면 세션 반환, 없으면 신규 세션을 생성
        // httpServletRequest.getSession(true); --> 기존 세션이있으면 기존세션반환, 세션이없으면 새로운 세션 생성
        // httpServletRequest.getSession(false); --> 기존 세션이있으면 기존세션반환, 세션이없어도 세션을 생성하지 않고 null 반환
        // 디폴트는 true 이기때문에 httpServletRequest.getSession() 으로 사용
        HttpSession session = httpServletRequest.getSession();
        // 세션에 로그인 회원 정볼르 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }


    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null)
            session.invalidate();
        return "redirect:/";
    }
}
