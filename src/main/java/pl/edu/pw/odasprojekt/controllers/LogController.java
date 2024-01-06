package pl.edu.pw.odasprojekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.pw.odasprojekt.services.LogService;

@Controller
@RequestMapping(path = "/logs")
public class LogController {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String history(Model model) {
        var clientNumber = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        var logs = logService.getClientLogs(clientNumber);

        model.addAttribute("logs", logs);

        return "logs";
    }
}
