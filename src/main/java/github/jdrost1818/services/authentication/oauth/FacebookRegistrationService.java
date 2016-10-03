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
public class FacebookRegistrationService implements RegistrationService<FacebookUser> {

    @Autowired
    protected FacebookRepository facebookUserRepository;

    @Override
    public User getUser(OAuth2Authentication authentication) {
        return this.getUser(facebookUserRepository, authentication);
    }

    @Override
    public User saveUser(OAuth2Authentication authentication) {
        LinkedHashMap userDetails = (LinkedHashMap) authentication.getUserAuthentication().getDetails();
        User userToSave = new User();

        userToSave.setFirstName(this.extractFirstName(userDetails));
        userToSave.setLastName(this.extractLastName(userDetails));

        FacebookUser facebookUser = new FacebookUser(userDetails.get("id").toString(), userToSave);

        return facebookUserRepository.save(facebookUser).getUser();
    }

    private String extractFirstName(LinkedHashMap userDetails) {
        String fullName = userDetails.get("name").toString();

        // This obviously won't work for names with spaces in them...
        return fullName.split(" ")[0];
    }

    private String extractLastName(LinkedHashMap userDetails) {
        String fullName = userDetails.get("name").toString();

        // This obviously won't work for names with spaces in them...
        return fullName.split(" ")[1];    }

}
