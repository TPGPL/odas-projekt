package pl.edu.pw.odasprojekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String details(@RequestParam(defaultValue = "false") boolean display, Model model) {
        var clientNumber = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        var user = service.getUserByClientNumber(clientNumber);

        model.addAttribute("shouldDisplay", display);
        model.addAttribute("name", anonymizeData(user.getPersonalData().getName(), display));
        model.addAttribute("surname", anonymizeData(user.getPersonalData().getSurname(), display));
        model.addAttribute("mail", anonymizeData(user.getPersonalData().getEmail(), display));
        model.addAttribute("pesel", anonymizeData(user.getPersonalData().getPESEL(), display));
        model.addAttribute("clientNumber", anonymizeData(user.getClientNumber(), display));
        model.addAttribute("cardNumber", anonymizeData(user.getBalance().getCardNumber(), display));
        model.addAttribute("cvv", anonymizeData(user.getBalance().getCvv(), display));
        model.addAttribute("expireDate", anonymizeData(user.getBalance().getExpireAt().toString(), display));

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

    private String anonymizeData(String data, boolean shouldDisplay) {
        if (shouldDisplay) {
            return data;
        }

        var anonData = new StringBuilder();
        int maxLength = Math.min(3, data.length() / 2);

        for (int i = 0; i < maxLength; i++) {
            anonData.append(data.charAt(i));
        }

        anonData.append("******");

        return anonData.toString();
    }
}
