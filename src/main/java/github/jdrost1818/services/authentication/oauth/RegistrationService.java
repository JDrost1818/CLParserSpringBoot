package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RegistrationService {

    User getUser(OAuth2Authentication authentication);

    User saveUser(OAuth2Authentication authentication);

}
