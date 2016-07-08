package github.jdrost1818.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestSearchCriteria {

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        String cat = "cat";
        String city = "city";
        String match = "match";
        String exclu = "exclusions";
        Integer minPrice = 0;
        Integer maxPrice = 100;

        SearchCriteria criteria = new SearchCriteria();
        criteria.setId(id);
        criteria.setCategory(cat);
        criteria.setCity(city);
        criteria.setMatch(match);
        criteria.setExclusions(exclu);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setSearchTitlesOnly(true);
        criteria.setHasPic(true);
        criteria.setPostedToday(true);
        criteria.setIncludeNearbyAreas(true);

        assertEquals(criteria.getId(), id);
        assertEquals(criteria.getCategory(), cat);
        assertEquals(criteria.getCity(), city);
        assertEquals(criteria.getMatch(), match);
        assertEquals(criteria.getExclusions(), exclu);
        assertEquals(criteria.getMinPrice(), minPrice);
        assertEquals(criteria.getMaxPrice(), maxPrice);
        assertTrue(criteria.isSearchTitlesOnly());
        assertTrue(criteria.isHasPic());
        assertTrue(criteria.isPostedToday());
        assertTrue(criteria.isIncludeNearbyAreas());
    }

}
