package hello.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;  // jakarta.validation 어떤 구현체든 동작 (Java Validator, Hibernate Validator)
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.Range;   // org.hibernate.validator 는 Hibernate Validator 에서만 동작.
import org.hibernate.validator.constraints.ScriptAssert;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10000 원 넘게 입력해주세요")
public class Item {

    @NotNull // 수정 요구사항 추가
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
//    @Max(9999) // 수정 요구사항 추가
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
