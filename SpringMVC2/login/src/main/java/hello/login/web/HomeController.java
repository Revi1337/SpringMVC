package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;


@Controller @RequiredArgsConstructor @Slf4j
public class HomeController {

    private final MemberRepository memberRepository;

    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) return "home";

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) return "home";

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // V2 ================================================================================================================================

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest httpServletRequest, Model model) {

        // 세션 관리장 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(httpServletRequest);

        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

    // V3 ================================================================================================================================

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest httpServletRequest, Model model) {

        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) return "home";

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 회원 데이터가 없으면 home
        if (loginMember == null) return "home";

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // V4 ================================================================================================================================

    @GetMapping("/")    // 스프링에서 제공하는 @SessionAttribute 기능은 세션을 생성하지 않음.
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                    Model model) {
        // 세션에 회원 데이터가 없으면 home
        if (loginMember == null) return "home";


        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

}