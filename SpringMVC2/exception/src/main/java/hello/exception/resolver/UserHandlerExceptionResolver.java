package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(UserHandlerExceptionResolver.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {
        try {
            if (UserException.class.isAssignableFrom(ex.getClass())) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
                response.setStatus(HttpStatus.BAD_REQUEST.value());

                if (acceptHeader.equals(MediaType.APPLICATION_JSON_VALUE)) {
                    HashMap<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());
                    String errorResultString = objectMapper.writeValueAsString(errorResult);

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
                    response.getWriter().println(errorResultString);
                    return new ModelAndView();
                } else {
                    // TEXT/HTML
                    return new ModelAndView("error/500");
                }
            }
        } catch (IOException exception) {
            log.error("resolver ex", exception);
        }
        return null;
    }
}
