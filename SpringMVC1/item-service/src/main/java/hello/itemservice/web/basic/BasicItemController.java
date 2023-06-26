package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    /**
     * V1 - @RequestParam 으로 모든 파라미터를 받은 후, Model 에 직접 등록
     *
     * @param itemName
     * @param price
     * @param quantity
     * @param model
     * @return
     */
//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam int quantity,
                            Model model) {
        Item item = new Item(itemName, price, quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * V2 - @ModelAttribute 를 사용하여 파라미터를 한번에 다 받음.
     * 하지만, Model 에 직접 등록해주어야한다는 귀찮은 아직 존재함.
     *
     * @param item
     * @param model
     * @return
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, Model model) {
        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * V3 - @ModelAttribute("item") 에 item 이라는 attr 를 설정하여, 자동으로 Model 에 item 이라는 이름의 attribute 를 추가시켜줌.
     *
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute("item") Item item) {
        itemRepository.save(item);

        return "basic/item";
    }

    /**
     * V4 - @ModelAttribute 에 attr 또한 설정하지않는 방법.
     * attr 을 설정하지 않으면, @ModelAttribute 를 달아준 Class 의 첫번쨰 글자만 소문자로 바꾼 item 이라는 이름으로 Model 에 담아줌
     *
     * @param item
     * @return
     */
//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item) {
        itemRepository.save(item);

        return "basic/item";
    }


    /**
     * V5 - 이게 최종. @ModelAttribute 마저 삭제.
     * - @ModelAttribute 만 사라진것이기 떄문에 V4 와 동일하게 Item 클래스의 첫글자를 소문자로 바꾼 item 이름으로 Model 에 담아줌.
     *
     * @param item
     * @return
     */
    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);

        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

}
