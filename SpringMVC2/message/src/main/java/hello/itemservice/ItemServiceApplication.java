package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	/**
	 * - 스프링에서 메시지 관리기능을 사용하려면 스프링이 제공하는 MessageSource 를 스프링 Bean 으로 등록해야한다.
	 * - MessageSource 는 interface 이기떄문에 따라서 구현체인 ResourceBundleMessageSource 를 스프링 빈으로 등록하면 된다.
	 * - 하지만, Spring Boot 에서 자동등록해주기떄문에 따로 Bean 을 등록해주지 않아도 된다.
	 *
	 * 1. .setBasenames() 는 설정파일들의 이름을 지정한다. resources 에서 {이름}.properties 파일을 읽어서 사용한다.
	 * 		추가적으로 메시지 국제화 기능을 적용하려면  {이름}_en.properties 처럼 파일명 마지막에 _en 로 언어정보를 주면 된다.
	 * 		만약 찾을 수 있는 국제화 파일이 없으면 messages.properties 를 기본으로 사용한다.
	 * 2. .setDefaultEncoding() 는 인코딩 정보를 지정한다. utf-8 를 사용하면 된다.
	 * @return
	 */
//	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
		resourceBundleMessageSource.setBasenames("messages", "errors");
		resourceBundleMessageSource.setDefaultEncoding("utf-8");
		return resourceBundleMessageSource;
	}

}
