package hello.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";

    private static final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse httpServletResponse) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        httpServletResponse.addCookie(mySessionCookie);
    }

    public Object getSession(HttpServletRequest httpServletRequest) {
        Cookie sessionCookie = findCookie(httpServletRequest, SESSION_COOKIE_NAME).orElse(null);
        if (sessionCookie == null)
            return null;
        return sessionStore.get(sessionCookie.getValue());
    }

    public void expire(HttpServletRequest httpServletRequest) {
        findCookie(httpServletRequest, SESSION_COOKIE_NAME)
                .ifPresent(cookie -> sessionStore.remove(cookie.getValue()));
    }

    public Optional<Cookie> findCookie(HttpServletRequest httpServletRequest, String cookieName) {
        if (httpServletRequest.getCookies() == null)
            return Optional.empty();
        return Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();
    }

}
