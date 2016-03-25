package github.jdrost1818.model;

import javax.persistence.*;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Entity
@Table(name = "CRAIGSLIST_SEARCH_CRITERIA")
public class SearchCriteria {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String category;
    private String city;
    private String match;
    private String exclusions;

    private Integer minPrice;
    private Integer maxPrice;

    private boolean searchTitlesOnly;
    private boolean hasPic;
    private boolean postedToday;
    private boolean includeNearbyAreas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getExclusions() {
        return exclusions;
    }

    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public boolean isHasPic() {
        return hasPic;
    }

    public void setHasPic(boolean hasPic) {
        this.hasPic = hasPic;
    }

    public boolean isSearchTitlesOnly() {
        return searchTitlesOnly;
    }

    public void setSearchTitlesOnly(boolean searchTitlesOnly) {
        this.searchTitlesOnly = searchTitlesOnly;
    }

    public boolean isPostedToday() {
        return postedToday;
    }

    public void setPostedToday(boolean postedToday) {
        this.postedToday = postedToday;
    }

    public boolean isIncludeNearbyAreas() {
        return includeNearbyAreas;
    }

    public void setIncludeNearbyAreas(boolean includeNearbyAreas) {
        this.includeNearbyAreas = includeNearbyAreas;
    }
}
