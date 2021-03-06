package github.jdrost1818.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestCraigslistCategory {

    @Test
    public void testContains() {
        CraigslistCategory category = CraigslistCategory.ALL;
        assertTrue(category.contains(category.all()));
        assertTrue(category.contains(category.dealer()));
        assertTrue(category.contains(category.owner()));
    }

    @Test
    public void testContainsDoesNotContain() {
        assertFalse(CraigslistCategory.ALL.contains("this is random"));
    }

    @Test
    public void testTitleFromKey() {
        String title = CraigslistCategory.ALL.title();
        String key1 = CraigslistCategory.ALL.all();
        String key2 = CraigslistCategory.ALL.dealer();
        String key3 = CraigslistCategory.ALL.owner();

        assertEquals(title, CraigslistCategory.titleFromKey(key1));
        assertEquals(title, CraigslistCategory.titleFromKey(key2));
        assertEquals(title, CraigslistCategory.titleFromKey(key3));
    }

    @Test
    public void testTitleFromKeyDNE() {
        assertNull(CraigslistCategory.titleFromKey("this is random"));
    }

}
