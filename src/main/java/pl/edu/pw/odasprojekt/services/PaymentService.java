package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.ServiceResponse;
import pl.edu.pw.odasprojekt.model.domain.Payment;
import pl.edu.pw.odasprojekt.model.dtos.PaymentDto;
import pl.edu.pw.odasprojekt.repositories.PaymentRepository;

import java.util.Date;
import java.util.regex.Pattern;

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

    public ServiceResponse<Void> createPayment(String senderNumber, PaymentDto dto) {
        var sentAt = new Date();

        if (!validatePaymentData(dto)) {
            return ServiceResponse.<Void>builder().success(false).build();
        }

        var sender = userService.getUserByClientNumber(senderNumber);

        if (sender.getBalance().getBalance() < dto.getAmount()) {
            return ServiceResponse.<Void>builder()
                    .success(false)
                    .message("Niewystarczające środki na koncie!").build();
        }

        var recipient = userService.getUserByClientNumber(dto.getRecipientNumber());

        var payment = Payment.builder()
                .amount(dto.getAmount())
                .title(dto.getTitle())
                .recipient(recipient)
                .sentAt(sentAt)
                .build();

        userService.adjustBalance(senderNumber, -dto.getAmount());
        userService.adjustBalance(recipient.getClientNumber(), dto.getAmount());

        paymentRepository.save(payment);

        return ServiceResponse.<Void>builder().success(true).build();
    }

    private boolean validatePaymentData(PaymentDto dto) {
        if (dto == null) {
            return false;
        }

        if (dto.getAmount() <= 0) {
            return false;
        }

        var clientNumber = dto.getRecipientNumber();

        if (clientNumber == null || clientNumber.length() != 13
                || !Pattern.compile("[0-9]{13}").matcher(clientNumber).find()
                || userService.getUserByClientNumber(clientNumber) == null) {
            return false;
        }

        var title = dto.getTitle();

        return title != null && !title.isEmpty() && title.length() <= 99
                && Pattern.compile("[A-Za-z0-9!.,? ]").matcher(title).find();
    }
}
