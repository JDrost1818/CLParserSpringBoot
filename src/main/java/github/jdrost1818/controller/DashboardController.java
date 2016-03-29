package github.jdrost1818.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by JAD0911 on 3/29/2016.
 */
@Controller
public class DashboardController {

    private static final String DASHBOARD = "dashboard/index";

    @RequestMapping("/dashboard")
    public String dashboard() {
        return DASHBOARD;
    }

}
