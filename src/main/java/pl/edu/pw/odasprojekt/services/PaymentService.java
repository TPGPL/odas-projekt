package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.ServiceResponse;
import pl.edu.pw.odasprojekt.model.domain.Payment;
import pl.edu.pw.odasprojekt.model.dtos.PaymentDto;
import pl.edu.pw.odasprojekt.repositories.PaymentRepository;

import java.util.Date;

import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.verifyCardNumber;
import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.verifyPaymentTitle;

@Service
public class PaymentService {
    private final UserService userService;
    private final BalanceService balanceService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(UserService userService, BalanceService balanceService, PaymentRepository paymentRepository) {
        this.userService = userService;
        this.balanceService = balanceService;
        this.paymentRepository = paymentRepository;
    }

    public ServiceResponse<Iterable<Payment>> getAllPaymentsForClient(String clientNumber) {
        var user = userService.getUserByClientNumber(clientNumber);
        var userId = user.getId();
        var payments = paymentRepository.findAllByRecipientIdOrSenderId(userId, userId);

        for (var payment : payments) {
            if (payment.getSender().getClientNumber().equals(clientNumber)) {
                payment.setAmount(payment.getAmount() * -1);
            }
        }

        return ServiceResponse.<Iterable<Payment>>builder().success(true).data(payments).build();
    }

    public ServiceResponse<Void> createPayment(String senderNumber, PaymentDto dto) {
        var sentAt = new Date();

        if (!validatePaymentData(dto)) {
            return ServiceResponse.<Void>builder().success(false).build();
        }

        if (balanceService.getUserBalance(senderNumber) < dto.getAmount()) {
            return ServiceResponse.<Void>builder()
                    .success(false)
                    .message("Niewystarczające środki na koncie!").build();
        }

        if (dto.getRecipientNumber().equals(balanceService.getUserCardNumber(senderNumber))) {
            return ServiceResponse.<Void>builder()
                    .success(false)
                    .message("Nie możesz przesłać srodków na własną kartę!").build();
        }

        var sender = userService.getUserByClientNumber(senderNumber);
        var recipient = userService.getUserByCardNumber(dto.getRecipientNumber());

        var payment = Payment.builder()
                .amount(dto.getAmount())
                .title(dto.getTitle())
                .recipient(recipient)
                .sender(sender)
                .sentAt(sentAt)
                .build();

        balanceService.adjustUserBalance(senderNumber, -dto.getAmount());
        balanceService.adjustUserBalance(recipient.getClientNumber(), dto.getAmount());

        paymentRepository.save(payment);

        return ServiceResponse.<Void>builder().success(true).build();
    }

    private boolean validatePaymentData(PaymentDto dto) {
        if (dto == null || dto.getAmount() <= 0) {
            return false;
        }

        var cardNumber = dto.getRecipientNumber();

        if (!verifyCardNumber(cardNumber) || !verifyPaymentTitle(dto.getTitle())) {
            return false;
        }

        return userService.getUserByCardNumber(cardNumber) != null;
    }
}
