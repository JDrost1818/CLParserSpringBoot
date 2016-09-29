package github.jdrost1818.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Controller
public class StaticController {

    private static final String HOME = "login.html";

    private static final String DASHBOARD = "dashboard.html";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return HOME;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return DASHBOARD;
    }

}
