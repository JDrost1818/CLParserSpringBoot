package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.FacebookUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.model.authentication.UserProvider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.LinkedHashMap;

import static java.util.Objects.nonNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RegistrationService {

    default User getUser(CrudRepository<UserProvider, String> repo, OAuth2Authentication authentication) {
        User user = null;
        Object authenticationDetails = authentication.getUserAuthentication().getDetails();
        if (nonNull(authenticationDetails) && authenticationDetails instanceof LinkedHashMap) {
            LinkedHashMap details = (LinkedHashMap) authenticationDetails;
            String oauthId = details.get("id").toString();
            UserProvider userProvider = repo.findOne(oauthId);
            user = nonNull(userProvider) ? userProvider.getUser() : null;
        }

        return user;
    }

    User saveUser(OAuth2Authentication authentication);

}
