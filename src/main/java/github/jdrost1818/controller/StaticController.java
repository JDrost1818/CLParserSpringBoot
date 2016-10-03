package github.jdrost1818.controller;

import github.jdrost1818.model.authentication.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static java.util.Objects.nonNull;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Controller
public class StaticController {

    @Autowired
    protected SessionUser sessionUser;

    private static final String LOGIN = "login.html";

    private static final String DASHBOARD = "dashboard.html";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return LOGIN;
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String dashboard() {
        return DASHBOARD;
    }

}
