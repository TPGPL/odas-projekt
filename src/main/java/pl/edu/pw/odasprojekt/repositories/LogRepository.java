package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pw.odasprojekt.model.domain.LogEvent;

public interface LogRepository extends CrudRepository<LogEvent, Integer> {
    Iterable<LogEvent> findAllByUserClientNumber(String clientNumber);
}
