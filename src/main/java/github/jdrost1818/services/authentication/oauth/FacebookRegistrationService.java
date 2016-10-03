package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.FacebookOAuthUserDetails;
import github.jdrost1818.model.authentication.FacebookUser;
import github.jdrost1818.model.authentication.GoogleUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.FacebookRepository;
import github.jdrost1818.repository.authentication.GoogleUserRepository;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
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

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(OAuth2Authentication authentication) {
        return this.getUser(facebookUserRepository, authentication);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUser(OAuth2Authentication authentication) {
        FacebookOAuthUserDetails userDetails = this.objectMapper.convertValue(
                authentication.getUserAuthentication().getDetails(), FacebookOAuthUserDetails.class);

        User userToSave = new User();
        userToSave.setFirstName(this.extractFirstName(userDetails.getName()));
        userToSave.setLastName(this.extractLastName(userDetails.getName()));

        FacebookUser facebookUser = new FacebookUser(userDetails.getId(), userToSave);

        return facebookUserRepository.save(facebookUser).getUser();
    }

    /**
     * Extracts the first name of the user's full concatenated name
     *
     * NOTE: assumes all words but the last is the first name
     *
     * @param fullName first and last name combined
     * @return user's first name
     */
    private String extractFirstName(String fullName) {
        return fullName.substring(0, fullName.lastIndexOf(" "));
    }

    /**
     * Extracts the last name of the user's full concatenated name
     *
     * NOTE: assumes the last word is the last name
     *
     * @param fullName first and last name combined
     * @return user's last name
     */
    private String extractLastName(String fullName) {
        return fullName.substring(fullName.lastIndexOf(" ") + 1);
    }

}
