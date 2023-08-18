package hello.typeconverter.converter;


import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

    @Test
    public void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer result = converter.convert("10");
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void integerToString() {
        IntegerToStringConverter integerToStringConverter = new IntegerToStringConverter();
        String result = integerToStringConverter.convert(123);
        assertThat(result).isEqualTo("123");
    }

    @Test
    public void ipPortToString() {
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);
        String result = converter.convert(source);
        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    public void stringToIpPort() {
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String ipAddress = "127.0.0.1:8080";
        IpPort result = converter.convert(ipAddress);
        assertThat(result.getIp()).isEqualTo("127.0.0.1");
        assertThat(result.getPort()).isEqualTo(8080);
    }
}
