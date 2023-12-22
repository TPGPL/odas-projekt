package pl.edu.pw.odasprojekt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
    @RequestMapping(path = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "user/dashboard";
    }

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public String details() {
        return "user/details";
    }
}
