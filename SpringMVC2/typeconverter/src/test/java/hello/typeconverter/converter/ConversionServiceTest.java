package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

// TODO 타입 컨버터를 하나하나 직접 찾아서 타입 변환에 사용하는 것은 매우 불편하다. 그래서 스프링은
//  개별 컨버터를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 제공하는데, 이것이 바로 컨버전
//  서비스( ConversionService )이다
public class ConversionServiceTest {

    @Test
    @DisplayName(value = "ConversionService 사용법")
    public void conversionService() {
        // 1. ConversionService 의 구현체인 DefaultConversionService 를 생성
        DefaultConversionService conversionService = new DefaultConversionService();

        // 2. DefaultConversionService 에 사용할 Converter 들을 등록
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 3. 사용 (source, 기대하는반환타입)
        assertThat(conversionService.convert("10", Integer.class))
                .isEqualTo(10);
        assertThat(conversionService.convert(10, String.class))
                .isEqualTo("10");
        assertThat(conversionService.convert("127.0.0.1:8080", IpPort.class))
                .isEqualTo(new IpPort("127.0.0.1", 8080));
        assertThat(conversionService.convert(new IpPort("127.0.0.1", 80), String.class))
                .isEqualTo("127.0.0.1:80");
    }
}
