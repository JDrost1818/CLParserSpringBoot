package github.jdrost1818.model.authentication;

import javax.persistence.*;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "FACEBOOK_USERS")
public class FacebookUser implements UserProvider {

    @Id
    @Column(name = "facebook_id")
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public FacebookUser() {
        // Default for hibernate
    }

    public FacebookUser(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
