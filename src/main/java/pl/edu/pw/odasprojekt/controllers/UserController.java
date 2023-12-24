package pl.edu.pw.odasprojekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.pw.odasprojekt.services.UserService;

@Controller
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }


    @RequestMapping(path = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        var clientNumber = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        var user = service.getUserByClientNumber(clientNumber);

        model.addAttribute("accountBalance", String.format("%.2f zł", user.getBalance().getBalance()));
        model.addAttribute("cardNumber", formatCardNumber(user.getBalance().getCardNumber()));

        return "user/dashboard";
    }

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public String details() {
        return "user/details";
    }

    private String formatCardNumber(String cardNumber) {
        int spacing = 4;
        var spacedNumber = new StringBuilder();

        for (int i = 0; i < cardNumber.length(); i++) {
            if (i % spacing == 0) {
                spacedNumber.append(" ");
            }

            spacedNumber.append(cardNumber.charAt(i));
        }

        return spacedNumber.toString();
    }
}
