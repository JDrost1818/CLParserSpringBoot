package github.jdrost1818.model;

import java.time.LocalDateTime;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestItem {

    @Test
    public void testIsValidNullCacheDate() {
        Item item = new Item();
        assertFalse(item.isStillValid());
    }

    @Test
    public void testIsValid() {
        Item item = new Item();
        item.setDateCached(LocalDateTime.now());
        assertTrue(item.isStillValid());
    }

    @Test
    public void testIsValidNotValid() {
        Item item = new Item();
        item.setDateCached(LocalDateTime.MIN);
        assertFalse(item.isStillValid());
    }
}
