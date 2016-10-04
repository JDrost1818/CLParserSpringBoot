package github.jdrost1818.model;

import github.jdrost1818.config.CacheConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

/**
 * Tasked with representing items which can be bought
 * through Craigslist (or potentially any service)
 *
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "ITEM_CACHE")
public class Item implements Serializable {

    @Id
    @Column(name = "id")
    private String name;

    private BigDecimal price;
    private LocalDateTime dateCached;

    public Item() {
        // Needed for JPA
    }

    public Item(String name) {
        setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDateCached() {
        return dateCached;
    }

    public void setDateCached(LocalDateTime dateCached) {
        this.dateCached = dateCached;
    }

    public boolean isStillValid() {
        if (isNull(getDateCached())) {
            return false;
        }
        LocalDateTime oldestValidCacheDate = LocalDateTime.now().minusDays(CacheConfig.DAYS_TO_CACHE);
        return oldestValidCacheDate.isBefore(getDateCached());
    }
}
