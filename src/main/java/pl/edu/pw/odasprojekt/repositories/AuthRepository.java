package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.odasprojekt.model.domain.UserAuth;

import java.util.Optional;

@Repository
public interface AuthRepository extends CrudRepository<UserAuth, Integer> {
    Optional<UserAuth> findByIndexAndUserId(int index, int id);

    Iterable<UserAuth> findAllByUserId(int id);
}
