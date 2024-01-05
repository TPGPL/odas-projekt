package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.odasprojekt.model.domain.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {
    Iterable<Payment> findAllByRecipientIdOrSenderId(int recipientId, int senderId);
}
