package github.jdrost1818.repository;

import github.jdrost1818.model.Item;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Component
public class ItemRepository {

    private HashMap<String, Item> db = new HashMap<>();

    public ItemRepository() {
        Item penny = new Item("penny");
        penny.setPrice(new BigDecimal(1));
        penny.setDateCached(new Date());
        db.put("penny", penny);
    }

    public void save(Item item) {
        db.put(item.getName(), item);
        System.out.println("SAVING " + item.getName() + " " + item.getPrice());
    }

    public Item findOne(String itemName) {
        if (db.containsKey(itemName))
            return db.get(itemName);
        return null;
    }
}
