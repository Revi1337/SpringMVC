package hello.itemservice.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

/**
 * DefaultMessageCodesResolver 의 기본 메시지 생성 규칙
 * 1. 객체 오류 (errorCode = required, ObjectName = item)
 *      --> 1. required.item
 *      --> 2. required
 * 2. 필드 오류 (errorCode = typeMismatch, ObjectName = user, field = age, fieldType = int )
 *      --> 1, typeMismatch.user.age
 *      --> 2. typeMismatch.age
 *      --> 3. typeMismatch.user.int
 *      --> 4. typeMismatch
 *
 * rejectValue(), reject() 는 내부에서 MessageCodesResolver 를 사용하며 여기에서 메시지코드들을 생성한다.
 */
public class MessageCodesResolverTest {

    MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();

    @Test
    @DisplayName("MessageCodesResolver 의 동작 [required] ObjectError")
    public void messageCodesResolverObject() {
        String[] messageCodes = messageCodesResolver.resolveMessageCodes("required", "item");
        System.out.println(Arrays.toString(messageCodes));
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    @DisplayName("MessageCodesResolver 의 동작 [required] FieldError")
    public void messageCodesResolverField() {
        String[] messageCodes = messageCodesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        System.out.println(Arrays.toString(messageCodes));
        assertThat(messageCodes).containsExactly("required.item.itemName", "required.itemName", "required.java.lang.String", "required");
    }
}
