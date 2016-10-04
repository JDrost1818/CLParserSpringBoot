package github.jdrost1818.controller;

import github.jdrost1818.model.authentication.SessionUser;
import github.jdrost1818.model.authentication.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
public class ExampleRestController {

    @Autowired
    protected SessionUser sessionUser;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public String fetchRandomJson() {
        return "Current User: " +  sessionUser.getName();
    }

}
