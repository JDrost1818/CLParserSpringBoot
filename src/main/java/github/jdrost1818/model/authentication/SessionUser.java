package github.jdrost1818.model.authentication;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionUser implements Serializable {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Long getId() {
        return currentUser.getId();
    }

    public void setId(Long id) {
        currentUser.setId(id);
    }

    public String getFirstName() {
        return currentUser.getFirstName();
    }

    public void setFirstName(String firstName) {
        currentUser.setFirstName(firstName);
    }

    public String getLastName() {
        return currentUser.getLastName();
    }

    public void setLastName(String lastName) {
        currentUser.setLastName(lastName);
    }

    public String getUsername() {
        return currentUser.getUsername();
    }

    public void setUsername(String username) {
        currentUser.setUsername(username);
    }

    public String getPassword() {
        return currentUser.getPassword();
    }

    public void setPassword(String password) {
        currentUser.setPassword(password);
    }

    public Date getLastParseTime() {
        return currentUser.getLastParseTime();
    }

    public void setLastParseTime(Date lastParseTime) {
        currentUser.setLastParseTime(lastParseTime);
    }

    public String getEmail() {
        return currentUser.getEmail();
    }

    public void setEmail(String email) {
        currentUser.setEmail(email);
    }

    public String getName() {
        return currentUser.getName();
    }

    public boolean implies(Subject subject) {
        return currentUser.implies(subject);
    }
}
