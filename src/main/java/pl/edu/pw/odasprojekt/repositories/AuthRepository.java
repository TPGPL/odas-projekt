package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pw.odasprojekt.model.domain.UserAuth;

import java.util.Optional;

public interface AuthRepository extends CrudRepository<UserAuth, Integer> {
    Optional<UserAuth> findByIndexAndUserId(int index, int id);
}
