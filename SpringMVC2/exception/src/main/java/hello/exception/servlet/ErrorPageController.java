package hello.exception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @Slf4j
public class ErrorPageController {

    // RequestDispatcher 에 정의되어 있음.
    private static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    private static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    private static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
    private static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    private static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    private static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        log.info("errorPage 404");
        printErrorInfo(httpServletRequest);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        log.info("errorPage 500");
        printErrorInfo(httpServletRequest);
        return "error-page/500";
    }

    private void printErrorInfo(HttpServletRequest httpServletRequest) {
        log.info("ERROR_EXCEPTION = {}", httpServletRequest.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE = {}", httpServletRequest.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE = {}", httpServletRequest.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI = {}", httpServletRequest.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME = {}", httpServletRequest.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE = {}", httpServletRequest.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType = {}", httpServletRequest.getDispatcherType());
    }

}
