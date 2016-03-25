package github.jdrost1818.model;

import github.jdrost1818.config.CacheConfig;
import github.jdrost1818.config.TimeConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JAD0911 on 3/24/2016.
 *
 * Tasked with representing items which can be bought
 * through Craigslist (or potentially any service)
 */
@Entity
@Table(name = "ITEM_CACHE")
public class Item implements Serializable {

    @Id
    @Column(name = "id")
    private String name;

    private BigDecimal price;
    private Date dateCached;

    public Item() {
        // Needed for JPA
    }

    public Item(String name) {
        this.name = name;
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

    public Date getDateCached() {
        return dateCached;
    }

    public void setDateCached(Date dateCached) {
        this.dateCached = dateCached;
    }

    public boolean isStillValid() {
        if (dateCached == null) {
            return false;
        }
        Date maxCacheDate = new Date(System.currentTimeMillis() - (CacheConfig.DAYS_TO_CACHE * TimeConfig.DAY_IN_MS));
        return maxCacheDate.compareTo(dateCached) <= 0;
    }

    @Override
    public String toString() {
        return String.format("%s: (%s) $%s", name, dateCached.toString(), price);
    }
}
