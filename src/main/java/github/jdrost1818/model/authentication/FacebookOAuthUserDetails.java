package github.jdrost1818.model.authentication;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class FacebookOAuthUserDetails {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
