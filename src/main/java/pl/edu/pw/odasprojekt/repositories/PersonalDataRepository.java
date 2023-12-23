package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.odasprojekt.model.domain.UserPersonalData;

import java.util.Optional;

@Repository
public interface PersonalDataRepository extends CrudRepository<UserPersonalData, Integer> {
    Optional<UserPersonalData> findByUserId(int id);
}
