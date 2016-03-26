package github.jdrost1818.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Jake on 3/25/2016.
 */
public class TestCraigslistCategory {

    @Test
    public void testContains() {
        CraigslistCategory category = CraigslistCategory.ALL;
        assert(category.contains(category.all()));
        assert(category.contains(category.dealer()));
        assert(category.contains(category.owner()));
    }

    @Test
    public void testContainsDoesNotContain() {
        assert(!CraigslistCategory.ALL.contains("this is random"));
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
