package hello.itemservice.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class MessageSourceTest {

    @Autowired MessageSource ms;

    @Test
    public void helloMessage() {
        String result = ms.getMessage("hello", null, null); // Locale 정보가 없으면 message.properties 가 동작한다.
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    @DisplayName("메시지소스 파일에 메시지 이름이 없으면 에외가 터진다")
    public void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    @DisplayName("메시지소스 파일에 메시지 이름이 없을 때 디폴트를 설정해 줄 수 있다.")
    public void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    @DisplayName("메시지파일에 메시지 이름에 arguments 들을 넘길 수 있다. {0} {1} ..")
    public void argumentMessage() {
        String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(result).isEqualTo("안녕 Spring");
    }

    @Test
    @DisplayName("메시지파일 디폴트 국제화")
    public void defaultLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");     // locale 없으니까 디폴트
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕"); // _ko 없으니가 디폴트
    }

    @Test
    @DisplayName("메시지파일 국제화 영어와 디폴트")
    public void enLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
