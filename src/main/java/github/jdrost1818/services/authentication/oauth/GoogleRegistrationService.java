package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.GoogleOAuthUserDetails;
import github.jdrost1818.model.authentication.GoogleUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.GoogleUserRepository;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class GoogleRegistrationService implements RegistrationService<GoogleUser> {

    @Autowired
    protected GoogleUserRepository googleUserRepository;

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(OAuth2Authentication authentication) {
        return this.getUser(googleUserRepository, authentication);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User saveUser(OAuth2Authentication authentication) {
        GoogleOAuthUserDetails userDetails = objectMapper.convertValue(authentication.getUserAuthentication().getDetails(), GoogleOAuthUserDetails.class);
        User userToSave = new User();

        userToSave.setFirstName(userDetails.getName().getGivenName());
        userToSave.setLastName(userDetails.getName().getFamilyName());
        userToSave.setEmail(userDetails.getEmails().get(0).getValue());

        GoogleUser googleUser = new GoogleUser(userDetails.getId(), userToSave);

        return googleUserRepository.save(googleUser).getUser();
    }

}
