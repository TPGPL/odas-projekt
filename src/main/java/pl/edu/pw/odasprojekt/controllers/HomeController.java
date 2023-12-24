package pl.edu.pw.odasprojekt.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        for (var aut : auth.getAuthorities()) {
            if (aut.getAuthority().equals("ROLE_USER")) {
                return "redirect:/dashboard";
            }
        }

        return "redirect:/auth/login";
    }
}
