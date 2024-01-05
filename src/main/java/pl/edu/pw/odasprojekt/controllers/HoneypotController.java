package pl.edu.pw.odasprojekt.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.pw.odasprojekt.services.LogService;

@Controller
public class HoneypotController {
    private final LogService logService;

    public HoneypotController(LogService logService) {
        this.logService = logService;
    }

    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    public String admin(HttpServletRequest request) {
        logService.createHoneypotLog(request, "/admin");

        return "bee/admin";
    }

    @RequestMapping(path = "/static", method = RequestMethod.GET)
    public String staticres(HttpServletRequest request) {
        logService.createHoneypotLog(request, "/static");

        return "bee/staticres";
    }

}
