package github.jdrost1818.util;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import static java.util.Objects.isNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public interface OAuthUtil {

    static boolean isNullRequest(OAuth2Authentication authentication) {
        return isNull(authentication)
                || isNull(authentication.getUserAuthentication())
                || isNull(authentication.getUserAuthentication().getDetails());
    }

}
