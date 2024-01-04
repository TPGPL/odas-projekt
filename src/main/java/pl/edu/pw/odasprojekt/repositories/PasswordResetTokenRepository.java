package pl.edu.pw.odasprojekt.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.odasprojekt.model.domain.PasswordResetToken;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Integer> {
    Optional<PasswordResetToken> findByToken(String token);
    boolean existsByToken(String token);
    Iterable<PasswordResetToken> findAllByUserClientNumber(String clientNumber);
}
