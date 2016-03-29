package github.jdrost1818.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Controller
public class StaticController {

    private static final String HOME = "index";

    @RequestMapping("/")
    public String home() {
        return HOME;
    }

}
