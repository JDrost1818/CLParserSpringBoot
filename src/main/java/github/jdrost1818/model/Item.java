package github.jdrost1818.model;

import github.jdrost1818.config.CacheConfig;
import github.jdrost1818.util.DateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JAD0911 on 3/24/2016.
 * <p>
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

    public Date getDateCached() {
        return dateCached;
    }

    public void setDateCached(Date dateCached) {
        this.dateCached = new Date(dateCached.getTime());
    }

    public boolean isStillValid() {
        if (getDateCached() == null) {
            return false;
        }
        Date oldestValidCacheDate = DateUtil.getXDaysAgo(CacheConfig.DAYS_TO_CACHE);
        return oldestValidCacheDate.before(getDateCached());
    }
}
