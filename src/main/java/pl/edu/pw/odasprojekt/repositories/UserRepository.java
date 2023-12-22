package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pw.odasprojekt.model.domain.UserData;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserData,Integer> {
    Optional<UserData> findByClientNumber(String clientNumber);
}
