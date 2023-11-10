package hello.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;  // jakarta.validation 어떤 구현체든 동작 (Java Validator, Hibernate Validator)
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.Range;   // org.hibernate.validator 는 Hibernate Validator 에서만 동작.

@Data
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
