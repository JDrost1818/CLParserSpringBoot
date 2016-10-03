package github.jdrost1818.model.authentication;

import javax.persistence.*;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "GOOGLE_USERS")
public class GoogleUser implements UserProvider {

    @Id
    @Column(name = "google_id")
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public GoogleUser() {
        // Default for hibernate
    }

    public GoogleUser(String id, User user) {
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
