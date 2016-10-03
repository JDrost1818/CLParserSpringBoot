package github.jdrost1818.model.authentication;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class PrincipalExtractor {

    private Principal principal;

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public List<String> getEmails() {
        if (nonNull(this.principal)) {
            if (principal instanceof OAuth2Authentication) {
                OAuth2Authentication auth = (OAuth2Authentication) principal;
                List<Map<String, String>> x = (List<Map<String, String>>) ((Map<String, Object>) auth.getUserAuthentication().getDetails()).get("emails");
                
                return x.stream()
                        .map(entry -> entry.get("value"))
                        .collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }
}
