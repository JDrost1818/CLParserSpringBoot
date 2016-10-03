package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.FacebookUser;
import github.jdrost1818.model.authentication.GoogleUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.FacebookRepository;
import github.jdrost1818.repository.authentication.GoogleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static java.util.Objects.nonNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class FacebookRegistrationService implements RegistrationService {

    @Autowired
    FacebookRepository facebookUserRepository;

    @Override
    public User getUser(OAuth2Authentication authentication) {
        return getUser(facebookUserRepository, authentication);
    }

    @Override
    public User saveUser(OAuth2Authentication authentication) {
        LinkedHashMap userDetails = (LinkedHashMap) authentication.getUserAuthentication().getDetails();
        User userToSave = new User();

        userToSave.setFirstName(this.extractFirstName(userDetails));
        userToSave.setLastName(this.extractLastName(userDetails));
        userToSave.setEmail(this.extractEmail(userDetails));

        GoogleUser googleUser = new GoogleUser(userDetails.get("id").toString(), userToSave);

        return facebookUserRepository.save(googleUser).getUser();
    }

    private String extractFirstName(LinkedHashMap userDetails) {
        return ((LinkedHashMap) userDetails.get("name")).get("givenName").toString();
    }

    private String extractLastName(LinkedHashMap userDetails) {
        return ((LinkedHashMap) userDetails.get("name")).get("familyName").toString();
    }

    @SuppressWarnings("unchecked")
    private String extractEmail(LinkedHashMap userDetails) {
        return ((ArrayList<LinkedHashMap<String, String>>) userDetails.get("emails")).get(0).get("value");
    }

}
