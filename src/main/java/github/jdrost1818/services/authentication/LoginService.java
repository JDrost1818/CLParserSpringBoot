package github.jdrost1818.services.authentication;

import github.jdrost1818.model.authentication.SessionUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.UserRepository;
import github.jdrost1818.services.authentication.oauth.FacebookRegistrationService;
import github.jdrost1818.services.authentication.oauth.GoogleRegistrationService;
import github.jdrost1818.services.authentication.oauth.RegistrationService;
import github.jdrost1818.util.OAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
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

    @Value("${facebook.client.clientId}")
    private String facebookClientId;

    @Autowired
    protected SessionUser sessionUser;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private GoogleRegistrationService googleRegistrationService;

    @Autowired
    private FacebookRegistrationService facebookRegistrationService;

    private Map<String, RegistrationService> clientRegistrationServiceMap = new HashMap<>();

    /**
     * Creates a map which maps client ids with the registration service for that client
     */
    @PostConstruct
    public void populateMap() {
        this.clientRegistrationServiceMap.put(googleClientId, googleRegistrationService);
        this.clientRegistrationServiceMap.put(facebookClientId, facebookRegistrationService);
    }

    /**
     * Gets the user from the database. If the user is not found in the
     * database, it will save the user first. This is expected to be used
     * for OAuth requests
     *
     * @param principal holds the user data
     * @return the {@link User} which is saved in the database
     */
    public User getUserSaveIfNotPresent(Principal principal) {
        User foundUser = sessionUser.getCurrentUser();

        if (isNull(foundUser) && principal instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) principal;
            foundUser = this.loadUser(authentication);

            // This means we have been able to authenticate
            // through OAuth, but the user is not yet saved
            // to the database for this app's use
            if (isNull(foundUser)) {
                foundUser = this.saveUser(authentication);
            }
        }

        sessionUser.setCurrentUser(foundUser);

        return foundUser;
    }

    /**
     * Extracts required fields for the DB, and saves the user
     *
     * @param authentication authentication object from an OAuth request
     * @return the user that was saved to the DB
     */
    public User saveUser(OAuth2Authentication authentication) {
        RegistrationService registrationService = this.getOAuthService(authentication);

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
        RegistrationService registrationService = this.getOAuthService(authentication);

        return nonNull(registrationService) ? registrationService.getUser(authentication) : null;
    }

    /**
     * Determines which {@link RegistrationService} to use for the given client id
     *
     * @param authentication authentication object from an OAuth request
     * @return the registration service for the given OAuth client
     */
    private RegistrationService getOAuthService(OAuth2Authentication authentication) {
        if (OAuthUtil.isNullRequest(authentication)) {
            return null;
        }
        String clientId = authentication.getOAuth2Request().getClientId();
        return nonNull(clientId) ? this.clientRegistrationServiceMap.get(clientId) : null;
    }

}
