package pl.edu.pw.odasprojekt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.pw.odasprojekt.model.dtos.UserLoginDto;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        var userLoginDto = UserLoginDto.builder().build();
        userLoginDto.selectPasswordFragments();

        model.addAttribute("user", userLoginDto);

        return "login";
    }

    @RequestMapping(path = "/forget-password", method = RequestMethod.GET)
    public String forgetPassword() {
        return "forget-password";
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.GET)
    public String changePassword() {
        return "change-password";
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.POST)
    public void forgetPasswordCommand() {
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public void loginCommand() {
    }
}
