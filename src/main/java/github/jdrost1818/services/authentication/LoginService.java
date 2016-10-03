package github.jdrost1818.services.authentication;

import github.jdrost1818.model.authentication.User;
import github.jdrost1818.services.authentication.oauth.GoogleRegistrationService;
import github.jdrost1818.services.authentication.oauth.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class LoginService {

    @Value("${google.client.clientId}")
    private String googleClientId;

    @Autowired
    private GoogleRegistrationService googleRegistrationService;

    private Map<String, RegistrationService> clientRegistrationServiceMap = new HashMap<>();

    /**
     * Creates a map which maps client ids with the registration service for that client
     */
    @PostConstruct
    public void populateMap() {
        this.clientRegistrationServiceMap.put(googleClientId, googleRegistrationService);
    }

    /**
     * Extracts required fields for the DB, and saves the user
     *
     * @param authentication authentication object from an OAuth request
     * @return the user that was saved to the DB
     */
    public User saveUser(OAuth2Authentication authentication) {
        RegistrationService registrationService = getOAuthService(authentication.getOAuth2Request().getClientId());

        return nonNull(registrationService) ? registrationService.saveUser(authentication) : null;
    }

    /**
     * Loads the information we have for the user attempting to
     * log in via OAuth
     *
     * @param authentication authentication object from an OAuth request
     * @return the user that is saved to the DB for the authentication
     */
    public User loadUser(OAuth2Authentication authentication) {
        RegistrationService registrationService = getOAuthService(authentication.getOAuth2Request().getClientId());

        return nonNull(registrationService) ? registrationService.getUser(authentication) : null;
    }

    /**
     * Determines which {@link RegistrationService} to use for the given client id
     *
     * @param clientId identifier for our OAuth clients
     * @return the registration service for the given OAuth client
     */
    private RegistrationService getOAuthService(String clientId) {
        return this.clientRegistrationServiceMap.get(clientId);
    }

}
