package github.jdrost1818.model.authentication;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
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

}
