package github.jdrost1818.controller;

import github.jdrost1818.model.authentication.LoginCredentials;
import github.jdrost1818.model.authentication.User;
import github.jdrost1818.repository.UserRepository;
import github.jdrost1818.services.authentication.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Created by daugherty on 9/28/16.
 */
@RestController
public class LoginController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected LoginService registrationService;

    @RequestMapping(value = "/login1", method = RequestMethod.POST)
    public User login(LoginCredentials credentials) {
        User userLoggingIn = userRepository.findByEmail(credentials.getUsername());
        if (nonNull(userLoggingIn) && Objects.equals(userLoggingIn.getPassword(), credentials.getPassword())) {
            return userLoggingIn;
        }
        return null;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(Principal principal) {
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
