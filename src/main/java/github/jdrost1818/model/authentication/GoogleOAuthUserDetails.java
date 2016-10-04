package github.jdrost1818.model.authentication;

import lombok.Data;

import java.util.List;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class GoogleOAuthUserDetails {

    private String id;

    private Name name;

    private List<Email> emails;

    @Data
    public static class Name {

        private String familyName;

        private String givenName;

    }

    @Data
    public static class Email {

        private String value;

    }

}
