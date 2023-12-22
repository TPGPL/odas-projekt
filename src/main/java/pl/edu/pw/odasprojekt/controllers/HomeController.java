package pl.edu.pw.odasprojekt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    // TODO: redirect to dashboard (maybe grantedAuthority = ROLE_USER)
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:/auth/login";
    }
}
