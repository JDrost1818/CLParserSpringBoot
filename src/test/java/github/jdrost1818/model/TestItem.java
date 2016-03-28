package github.jdrost1818.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by JAD0911 on 3/24/2016.
 */
public class TestItem {

    @Test
    public void testIsValidNullCacheDate() {
        Item item = new Item();
        assertTrue(!item.isStillValid());
    }

    @Test
    public void testIsValid() {
        Item item = new Item();
        item.setDateCached(new Date());
        assertTrue(item.isStillValid());
    }

    @Test
    public void testIsValidNotValid() {
        Item item = new Item();
        item.setDateCached(new Date(0));
        assertTrue(!item.isStillValid());
    }
}
