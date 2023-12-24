package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.ServiceResponse;
import pl.edu.pw.odasprojekt.model.domain.Payment;
import pl.edu.pw.odasprojekt.repositories.PaymentRepository;

@Service
public class PaymentService {
    private final UserService userService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(UserService userService, PaymentRepository paymentRepository) {
        this.userService = userService;
        this.paymentRepository = paymentRepository;
    }

    public ServiceResponse<Iterable<Payment>> getAllPaymentsForClient(String clientNumber) {
        var user = userService.getUserByClientNumber(clientNumber);

        var payments = paymentRepository.findAllByRecipientId(user.getId());

        return ServiceResponse.<Iterable<Payment>>builder().success(true).data(payments).build();
    }
}
