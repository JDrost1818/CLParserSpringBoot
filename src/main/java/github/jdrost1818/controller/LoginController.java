package github.jdrost1818.controller;

import github.jdrost1818.services.authentication.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
public class LoginController {

    @Autowired
    protected LoginService registrationService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return registrationService.getUserSaveIfNotPresent(principal);
    }

}
