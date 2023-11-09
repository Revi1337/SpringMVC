package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    /**
     * ValidationItemControllerV2 컨트롤러에 요청이 들어올때마다 WebDataBinder 가 새로 만들어지고, WebDataBinder 에 Validator 를 넣어둔다.
     * --> 따라서 모든 컨트롤러 내부의 모든 메서드가 호출될떄마다 Validator 가 동작한다
     * @param webDataBinder
     */
    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /**
     * new FieldError(String objectName, String field, String defaultMessage)
     * - objectName : @ModelAttribute 이름
     * - field : 오류가 발생한 필드 이름
     * - defaultMessage : 오류 기본 메시지
     *
     * BindingResult 는 검증할 대상 바로 다음에 와야 한다. 순서가 중요하다. @ModelAttribute Item item 바로 다음에 BindingResult 가 와야 한다.
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item,
                            BindingResult bindingResult, // 순서 중요. @ModelAttribute Item item 다음에 와야 한다.
                            RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,1000 원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * FieldError 는 두가지 생성자를 제공
     * 1. FieldError(String objectName, String field, String defaultMessage)
     * 2. FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
     *
     * - objectName : 오류가 발생한 객체 이름
     * - field : 오류 필드
     * - rejectedValue : 사용자가 입력한 값(거절된 값) --> 값 유지에 사용
     * - bindingFailure : 타입 오류 같은 바인딩 실패인지(true), 바인딩은 성공했는데 검증 실패인지(false) 의 구분 값
     * - codes : 메시지 코드
     * - arguments : 메시지에서 사용하는 인자
     * - defaultMessage : 기본 오류 메시지
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,1000 원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item",
                    "itemName",
                    item.getItemName(),
                    false,
                    new String[]{"required.item.itemName"},
                    null,
                    null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item",
                    "price",
                    item.getPrice(),
                    false,
                    new String[]{"range.item.price"},
                    new Object[]{1000, 1000000},
                    null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item",
                    "quantity",
                    item.getQuantity(),
                    false,
                    new String[]{"max.item.quantity"},
                    new Object[]{9999},
                    null));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",
                        new String[]{"totalPriceMin"},
                        new Object[]{10000, resultPrice},
                        null));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * reject() 는 Object 이고, rejectValue() 는 Field 이다.
     * - rejectValue(), reject() 의 errorCode 는 MessageCodeResolver 를 통해 해석되고 동작한다.
     *
     * - rejectValue(), reject() 의 errorCode 를 사용하면 "errorCode.objectName.fieldName" 포맷으로 에러코드를 찾는다.
     * - errorCode.objectName.fieldName 로 이루어진 메시지가 있으면 해당 메시지가 우선순위를 갖게되고, 없으면, errorCode 라는 메시지가 사용된다.
     * - --> 즉, 계층적인 구조를 갖게되고, 우선순위에 따라 사용할 수 있다는 것이다. 이것을 가능하게 하는 것이 MessageCodesResolver 이다.
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required"); // line 229~231 와 같음
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * Custom Validator 로 검증로직 분리
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (itemValidator.supports(item.getClass())) {
            itemValidator.validate(item, bindingResult);
        }
        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

