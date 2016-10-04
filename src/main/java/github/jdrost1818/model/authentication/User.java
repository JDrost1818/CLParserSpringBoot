package github.jdrost1818.model.authentication;

import lombok.Data;

import javax.persistence.*;
import java.security.Principal;
import java.util.Date;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "USERS")
public class User implements Principal {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private Date lastParseTime = new Date(0);

    @Override
    @Transient
    public String getName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
