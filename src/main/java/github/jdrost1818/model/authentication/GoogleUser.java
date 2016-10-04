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

}
