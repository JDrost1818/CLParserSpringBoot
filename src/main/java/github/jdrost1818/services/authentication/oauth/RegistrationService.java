package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.DefaultOAuthResponse;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.model.authentication.UserProvider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.LinkedHashMap;

import static github.jdrost1818.util.ObjectMapperUtil.mapOauthResponse;
import static java.util.Objects.nonNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RegistrationService<T extends UserProvider> {

    /**
     * Consolidates the getUser logic, just takes the repository in which to search.
     *
     * @param authentication authentication object from an OAuth request
     * @return the corresponding user for the authentication
     * @see RegistrationService#getUser(OAuth2Authentication)
     */
    default User getUser(CrudRepository<T, String> repo, OAuth2Authentication authentication) {
        User user = null;
        if (nonNull(authentication)) {
            // If the user isn't null, get the details from the authentication
            Object authenticationDetails = authentication.getUserAuthentication().getDetails();
            if (nonNull(authenticationDetails) && authenticationDetails instanceof LinkedHashMap) {
                // Parses the authentication details into an object from
                // which we can get an id so we can fetch it from the database
                DefaultOAuthResponse response = mapOauthResponse(authenticationDetails, DefaultOAuthResponse.class);
                UserProvider userProvider = repo.findOne(response.getId());

                user = nonNull(userProvider) ? userProvider.getUser() : null;
            }
        }

        return user;
    }

    /**
     * Finds the user corresponding the authentication provided. Will
     * return null if there is not a corresponding user for the
     * provided authentication in the database.
     *
     * @param authentication authentication object from an OAuth request
     * @return the corresponding user for the authentication
     */
    User getUser(OAuth2Authentication authentication);

    /**
     * Creates a new {@link User} with the information it can find
     * from the given authentication and saves it to the database
     *
     * @param authentication authentication object from an OAuth request
     * @return the user saved to the database
     */
    User saveUser(OAuth2Authentication authentication);

}
