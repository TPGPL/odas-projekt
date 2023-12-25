package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.odasprojekt.model.domain.UserData;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserData, Integer> {
    Optional<UserData> findByClientNumber(String clientNumber);

    Optional<UserData> findByBalanceCardNumber(String cardNumber);

    boolean existsByClientNumber(String clientNumber);
}
