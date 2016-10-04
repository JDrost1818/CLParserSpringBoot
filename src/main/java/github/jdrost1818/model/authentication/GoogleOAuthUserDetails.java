package github.jdrost1818.model.authentication;

import java.util.List;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class GoogleOAuthUserDetails {

    private String id;

    private Name name;

    private List<Email> emails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public static class Name {

        private String familyName;

        private String givenName;

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }
    }

    public static class Email {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
