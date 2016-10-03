package github.jdrost1818.model.authentication;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginCredentials {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
