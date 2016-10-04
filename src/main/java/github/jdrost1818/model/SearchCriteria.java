package github.jdrost1818.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "SEARCH_CRITERIA")
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

}
