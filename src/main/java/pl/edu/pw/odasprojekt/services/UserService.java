package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.ServiceResponse;
import pl.edu.pw.odasprojekt.model.domain.UserData;
import pl.edu.pw.odasprojekt.model.dtos.UserLoginDto;
import pl.edu.pw.odasprojekt.repositories.UserRepository;

import java.util.Date;

import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.verifyClientNumber;
import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.verifyPasswordFragments;

@Service
public class UserService {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_TIME = 24 * 60 * 60 * 1000; // 24 hours
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserData getUserByClientNumber(String clientNumber) {
        return userRepository.findByClientNumber(clientNumber).orElse(null);
    }

    public UserData getUserByCardNumber(String cardNumber) {
        return userRepository.findByBalanceCardNumber(cardNumber).orElse(null);
    }

    public ServiceResponse<String> login(UserLoginDto dto) {
        if (!validateLoginDto(dto) || !userRepository.existsByClientNumber(dto.getClientNumber())) {
            return ServiceResponse.<String>builder().success(false).build();
        }

        var user = getUserByClientNumber(dto.getClientNumber());

        if (user.isLocked() && !shouldUnlock(user)) {
            return ServiceResponse.<String>builder()
                    .message("Twoje konto zostało zablokowane. Spróbuj ponownie później.")
                    .success(false).build();
        }

        long calculatedSecret = authService.calculateLoginSecret(user.getId(), dto.getPasswordFrags());

        String givenPassword = calculatedSecret + user.getSecretSalt();

        if (passwordEncoder.matches(givenPassword, user.getSecretHash())) {
            var jwtToken = jwtService.generateJwtForUser(dto.getClientNumber());

            user.setFailedLoginAttempts(0);
            userRepository.save(user);

            return ServiceResponse.<String>builder().success(true).data(jwtToken).build();
        }

        var failedAttempts = user.getFailedLoginAttempts();

        if (failedAttempts == MAX_FAILED_ATTEMPTS) {
            lockAccount(user);

            return ServiceResponse.<String>builder()
                    .message("Twoje konto zostało zablokowane. Spróbuj ponownie później.")
                    .success(false).build();
        }

        user.setFailedLoginAttempts(failedAttempts + 1);
        userRepository.save(user);

        return ServiceResponse.<String>builder().success(false).build();
    }

    private boolean validateLoginDto(UserLoginDto dto) {
        return dto != null && verifyClientNumber(dto.getClientNumber()) && verifyPasswordFragments(dto.getPasswordFrags());
    }

    private boolean shouldUnlock(UserData user) {
        if (new Date().getTime() > user.getLockedUntil().getTime()) {
            user.setLocked(false);
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);

            userRepository.save(user);

            return true;
        }

        return false;
    }

    private void lockAccount(UserData user) {
        var currentDate = new Date();
        var lockDate = new Date(currentDate.getTime() + LOCK_TIME);

        user.setLocked(true);
        user.setLockedUntil(lockDate);

        userRepository.save(user);
    }
}
