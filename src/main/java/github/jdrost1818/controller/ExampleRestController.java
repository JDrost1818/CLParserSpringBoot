package github.jdrost1818.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by daugherty on 9/28/16.
 */
@RestController
public class ExampleRestController {

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public String fetchRandomJson() {
        return "This is some custom text - FROM REST";
    }

}
