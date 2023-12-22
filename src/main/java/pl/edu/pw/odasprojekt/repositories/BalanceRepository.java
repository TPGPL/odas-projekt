package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pw.odasprojekt.model.domain.UserBalance;

import java.util.Optional;

public interface BalanceRepository extends CrudRepository<UserBalance, Integer> {
    Optional<UserBalance> findByUserId(int id);
}