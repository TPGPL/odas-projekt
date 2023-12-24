package pl.edu.pw.odasprojekt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.pw.odasprojekt.model.dtos.PaymentDto;
import pl.edu.pw.odasprojekt.services.PaymentService;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String paymentsHistory(Model model) {
        var clientNumber = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        var payments = service.getAllPaymentsForClient(clientNumber).getData();

        model.addAttribute("payments", payments);

        return "payments/history";
    }

    @RequestMapping(path = "/new", method = RequestMethod.GET)
    public String createPayment(Model model) {
        model.addAttribute("payment", new PaymentDto());

        return "payments/new";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createPaymentCommand() {
    }
}
