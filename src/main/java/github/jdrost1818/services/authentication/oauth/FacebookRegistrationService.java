package github.jdrost1818.services.authentication.oauth;

import github.jdrost1818.model.authentication.FacebookOAuthUserDetails;
import github.jdrost1818.model.authentication.FacebookUser;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.authentication.FacebookUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import static github.jdrost1818.util.ObjectMapperUtil.mapOauthResponse;
import static java.util.Objects.isNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class FacebookRegistrationService implements RegistrationService<FacebookUser> {

    @Autowired
    protected FacebookUserRepository facebookUserRepository;

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
        // Early exit on null input
        if (isNull(authentication)
                || isNull(authentication.getUserAuthentication())
                || isNull(authentication.getUserAuthentication().getDetails())) {
            return null;
        }

        Object userDetailMap = authentication.getUserAuthentication().getDetails();
        FacebookOAuthUserDetails userDetails = mapOauthResponse(userDetailMap, FacebookOAuthUserDetails.class);

        if (isNull(userDetails.getId())) {
            return null;
        }
        User userToSave = new User();
        userToSave.setFirstName(this.extractFirstName(userDetails.getName()));
        userToSave.setLastName(this.extractLastName(userDetails.getName()));

        FacebookUser facebookUserToSave = new FacebookUser(userDetails.getId(), userToSave);
        return facebookUserRepository.save(facebookUserToSave).getUser();
    }

    /**
     * Extracts the first name of the user's full concatenated name
     * <p>
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
     * <p>
     * NOTE: assumes the last word is the last name
     *
     * @param fullName first and last name combined
     * @return user's last name
     */
    private String extractLastName(String fullName) {
        return fullName.substring(fullName.lastIndexOf(" ") + 1);
    }

}
