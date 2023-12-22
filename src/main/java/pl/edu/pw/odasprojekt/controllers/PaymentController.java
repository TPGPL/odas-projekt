package pl.edu.pw.odasprojekt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    @RequestMapping(method = RequestMethod.GET)
    public String paymentsHistory() {
        return "payments/history";
    }

    @RequestMapping(path="/new" ,method = RequestMethod.GET)
    public String createPayment() {
        return "payments/new";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createPaymentCommand() {
    }
}
