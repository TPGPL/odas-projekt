package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pw.odasprojekt.model.domain.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
    Iterable<Payment> findAllByRecipientId(int id);
}
