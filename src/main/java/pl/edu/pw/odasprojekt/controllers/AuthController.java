package pl.edu.pw.odasprojekt.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.edu.pw.odasprojekt.model.dtos.ForgetPasswordDto;
import pl.edu.pw.odasprojekt.model.dtos.UserLoginDto;
import pl.edu.pw.odasprojekt.services.PasswordResetService;
import pl.edu.pw.odasprojekt.services.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordResetService resetService;

    @Autowired
    public AuthController(UserService userService, PasswordResetService resetService) {
        this.userService = userService;
        this.resetService = resetService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        var userLoginDto = UserLoginDto.builder().build();
        userLoginDto.selectPasswordFragments();

        model.addAttribute("user", userLoginDto);

        return "login";
    }

    @RequestMapping(path = "/forget-password", method = RequestMethod.GET)
    public String forgetPassword(Model model) {
        var forgetPasswordDto = new ForgetPasswordDto();

        model.addAttribute("data", forgetPasswordDto);

        return "forget-password";
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.GET)
    public String changePassword() {
        return "change-password";
    }

    @RequestMapping(path = "/forget-password", method = RequestMethod.POST)
    public String forgetPasswordCommand(@ModelAttribute ForgetPasswordDto data, RedirectAttributes redirectAttributes) {
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {
        }

        resetService.handleForgetRequest(data);

        redirectAttributes.addFlashAttribute("wasSent", true);

        return "redirect:/auth/forget-password";
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.POST)
    public void changePasswordCommand() {
    }

    // TODO: Redirect to dashboard if logged
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginCommand(@ModelAttribute UserLoginDto user, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            Thread.sleep(2000);
        } catch(Exception ignored) {
        }

        var resp = userService.login(user);

        if (!resp.isSuccess()) {
            // TODO: Decide if account block message should actually be shown
            //var message = resp.getMessage() != null ? resp.getMessage() : "Nieprawidłowe dane logowania.";
            redirectAttributes.addFlashAttribute("message", "Nieprawidłowe dane logowania.");

            return "redirect:/auth/login";
        }

        var jwt = new Cookie("jwtToken", resp.getData());

        jwt.setHttpOnly(true);
        //jwt.setSecure(true); // TODO
        jwt.setMaxAge(10 * 60);
        jwt.setPath("/");

        response.addCookie(jwt);

        return "redirect:/dashboard";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletResponse response) {
        var jwt = new Cookie("jwtToken", null);
        jwt.setPath("/");
        jwt.setHttpOnly(true);
        jwt.setMaxAge(0);
        //jwt.setSecure(true); // TODO

        response.addCookie(jwt);

        return "redirect:/";
    }
}
