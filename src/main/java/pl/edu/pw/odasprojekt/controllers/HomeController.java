package pl.edu.pw.odasprojekt.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static pl.edu.pw.odasprojekt.utils.AuthUtils.isAuthorized;

@Controller
public class HomeController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (isAuthorized(auth)) {
            return "redirect:/dashboard";
        }

        return "redirect:/auth/login";
    }
}
