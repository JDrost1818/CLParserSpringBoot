package github.jdrost1818.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by daugherty on 9/28/16.
 */
@RestController
public class LoginController {

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }

}
