package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.GoogleOAuthUserDetails;
import github.jdrost1818.model.authentication.GoogleUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.GoogleUserRepository;
import github.jdrost1818.util.OAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static github.jdrost1818.util.ObjectMapperUtil.mapOauthResponse;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class GoogleRegistrationService implements RegistrationService<GoogleUser> {

    @Autowired
    protected GoogleUserRepository googleUserRepository;

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
        // Early exit on null input
        if (OAuthUtil.isNullRequest(authentication)) {
            return null;
        }

        // Map the response into a google response-specific object
        Object userDetailMap = authentication.getUserAuthentication().getDetails();
        GoogleOAuthUserDetails userDetails = mapOauthResponse(userDetailMap, GoogleOAuthUserDetails.class);

        // Extract the properties from the parsed response
        User userToSave = new User();
        if (isNull(userDetails.getId())) {
            return null;
        }
        if (nonNull(userDetails.getName())) {
            userToSave.setFirstName(userDetails.getName().getGivenName());
            userToSave.setLastName(userDetails.getName().getFamilyName());
        }
        if (!CollectionUtils.isEmpty(userDetails.getEmails())) {
            userToSave.setEmail(userDetails.getEmails().get(0).getValue());
        }

        GoogleUser googleUserToSave = new GoogleUser(userDetails.getId(), userToSave);
        googleUserToSave = this.googleUserRepository.save(googleUserToSave);
        return googleUserToSave.getUser();
    }

}
