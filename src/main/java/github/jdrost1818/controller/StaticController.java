package github.jdrost1818.controller;

import github.jdrost1818.model.authentication.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
public class StaticController {

    @Autowired
    protected SessionUser sessionUser;

    private static final String LOGIN = "login.html";

    private static final String DASHBOARD = "dashboard.html";

    private static final String REDIRECT_HOME = "redirect:home";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return REDIRECT_HOME;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String dashboard() {
        return DASHBOARD;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String login() {
        return LOGIN;
    }

}
