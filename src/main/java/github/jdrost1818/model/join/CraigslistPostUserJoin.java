package github.jdrost1818.model.join;

import github.jdrost1818.model.User;
import github.jdrost1818.model.craigslist.CraigslistPost;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jake on 3/24/2016.
 */
@Entity
@Table(name = "CRAIGSLIST_POST_USER_JOIN")
public class CraigslistPostUserJoin {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private CraigslistPost craigslistPost;

    private Date dateLastAccessed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateLastAccessed() {
        return dateLastAccessed;
    }

    public void setDateLastAccessed(Date dateLastAccessed) {
        this.dateLastAccessed = dateLastAccessed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CraigslistPost getCraigslistPost() {
        return craigslistPost;
    }

    public void setCraigslistPost(CraigslistPost craigslistPost) {
        this.craigslistPost = craigslistPost;
    }
}
