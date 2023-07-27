package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "SESSION TEST")
class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() {
        // 세션 생성
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, mockHttpServletResponse);

        // 요청에 응답 쿠키 저장
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(mockHttpServletResponse.getCookies());

        // 세션 조회
        Object result = sessionManager.getSession(mockHttpServletRequest);
        assertThat(result).isEqualTo(member);

        // 세션 만료
        sessionManager.expire(mockHttpServletRequest);
        Object expired = sessionManager.getSession(mockHttpServletRequest);
        assertThat(expired).isNull();
    }

}