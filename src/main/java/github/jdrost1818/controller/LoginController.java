package github.jdrost1818.controller;

import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.UserRepository;
import github.jdrost1818.services.authentication.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.sso.EnableOAuth2Sso;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static java.util.Objects.isNull;

/**
 * Created by daugherty on 9/28/16.
 */
@RestController
@EnableOAuth2Sso
public class LoginController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected LoginService registrationService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        User foundUser = null;
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) principal;
            foundUser = registrationService.loadUser(authentication);
            if (isNull(foundUser)) {
                foundUser = registrationService.saveUser(authentication);
            }
        }

        return foundUser;
    }

}
