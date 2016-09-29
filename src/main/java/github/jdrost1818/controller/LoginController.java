package github.jdrost1818.controller;

import github.jdrost1818.model.LoginCredentials;
import github.jdrost1818.model.PrincipalExtractor;
import github.jdrost1818.model.User;
import github.jdrost1818.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Created by daugherty on 9/28/16.
 */
@RestController
public class LoginController {

    @Autowired
    protected UserRepository userRepository;

    @RequestMapping(value = "/login1", method = RequestMethod.POST)
    public User login(LoginCredentials credentials) {
        User userLoggingIn = userRepository.findByEmail(credentials.getUsername());
        if (nonNull(userLoggingIn) && Objects.equals(userLoggingIn.getPassword(), credentials.getPassword())) {
            return userLoggingIn;
        }
        return null;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        PrincipalExtractor extractor = new PrincipalExtractor();
        extractor.setPrincipal(principal);
        List<String> emails = extractor.getEmails();
        if (!isEmpty(emails)) {

        }
        return principal;
    }

}
