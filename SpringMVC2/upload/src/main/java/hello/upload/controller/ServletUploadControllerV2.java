package hello.upload.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j @RequiredArgsConstructor
@Controller @RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request = {}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        Collection<Part> requestParts = request.getParts();
        log.info("requestParts = {}", requestParts);

        for (Part requestPart : requestParts) {
            log.info("=======================================");
            log.info("name = {}", requestPart.getName());                               // name= 의 값을 가져온다.
            Collection<String> headerNames = requestPart.getHeaderNames();              // Multipart 에서 사용된 Header 들을 가져온다. (Content-Disposition, Content-Type 등)
            for (String headerName : headerNames) {
                log.info("header = {} : {}", headerName, requestPart.getHeader(headerName));
            }
            // 편의 메서드
            // content-disposition; filename=asd
            log.info("submittedFilename = {}", requestPart.getSubmittedFileName());     // filename= 의 값을 가져온다. (업로드된 파일 이름을 의미한다.)
            log.info("size = {}", requestPart.getSize());                               // part body size (업로드된 파일 사이즈를 의미한다.)

            // 데이터 읽기
            ServletInputStream inputStream = request.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//            log.info("body = {}", body);

            // 파일에 저장하기
            if (StringUtils.hasText(requestPart.getSubmittedFileName())) {
                String fullPath = fileDir + requestPart.getSubmittedFileName();
                log.info("파일 저장 fullPath = {}", fullPath);
                requestPart.write(fullPath);                                            // Servlet 이 제공하는 Part 는 write() 을 통해 특정경로에 바로 저장할 수 있다.
            }
        }

        return "upload-form";
    }

}
