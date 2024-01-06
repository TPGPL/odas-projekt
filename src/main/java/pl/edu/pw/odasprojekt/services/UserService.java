package pl.edu.pw.odasprojekt.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.ServiceResponse;
import pl.edu.pw.odasprojekt.model.domain.EventType;
import pl.edu.pw.odasprojekt.model.domain.UserData;
import pl.edu.pw.odasprojekt.model.dtos.PasswordFragmentDto;
import pl.edu.pw.odasprojekt.model.dtos.UserLoginDto;
import pl.edu.pw.odasprojekt.repositories.UserRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.*;

@Service
public class UserService {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_TIME = 24 * 60 * 60 * 1000; // 24 hours
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final LogService logService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, LogService logService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.logService = logService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserData getUserByClientNumber(String clientNumber) {
        return userRepository.findByClientNumber(clientNumber).orElse(null);
    }

    public UserData getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserData getUserByCardNumber(String cardNumber) {
        return userRepository.findByBalanceCardNumber(cardNumber).orElse(null);
    }

    public UserData getUserByEmail(String email) {
        return userRepository.findByPersonalDataEmail(email).orElse(null);
    }

    public ServiceResponse<String> login(HttpServletRequest request, UserLoginDto dto) {
        if (!validateLoginDto(dto) || !userRepository.existsByClientNumber(dto.getClientNumber())) {
            return ServiceResponse.<String>builder().success(false).build();
        }

        var user = getUserByClientNumber(dto.getClientNumber());

        if (user.isLocked() && !shouldUnlock(user)) {
            logService.createUserLog(request, EventType.LoginFailure, user);

            return ServiceResponse.<String>builder()
                    .message("Twoje konto zostało zablokowane. Spróbuj ponownie później.")
                    .success(false).build();
        }

        PasswordFragmentDto[] passwordFrags = {dto.getS1(), dto.getS2(), dto.getS3()};
        long calculatedSecret = authService.calculateLoginSecret(user.getId(), passwordFrags);
        String givenPassword = calculatedSecret + user.getSecretSalt();

        if (passwordEncoder.matches(givenPassword, user.getSecretHash())) {
            var jwtToken = jwtService.generateJwt(dto.getClientNumber());

            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            logService.createUserLog(request, EventType.LoginSuccess, user);

            return ServiceResponse.<String>builder().success(true).data(jwtToken).build();
        }

        var failedAttempts = user.getFailedLoginAttempts();

        if (failedAttempts == MAX_FAILED_ATTEMPTS) {
            lockAccount(user);
            logService.createUserLog(request, EventType.LoginFailure, user);

            return ServiceResponse.<String>builder()
                    .message("Twoje konto zostało zablokowane. Spróbuj ponownie później.")
                    .success(false).build();
        }

        user.setFailedLoginAttempts(failedAttempts + 1);
        userRepository.save(user);
        logService.createUserLog(request, EventType.LoginFailure, user);

        return ServiceResponse.<String>builder().success(false).build();
    }

    public void updatePassword(int userId, int K) {
        var user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return;
        }

        String newPassword = K + user.getSecretSalt();

        user.setSecretHash(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    private boolean validateLoginDto(UserLoginDto dto) {
        if (dto == null || !verifyClientNumber(dto.getClientNumber())) {
            return false;
        }

        var indexes = new HashSet<Integer>();
        var passFrags = List.of(dto.getS1(), dto.getS2(), dto.getS3());

        for (var pass : passFrags) {
            if (!verifyPasswordFragment(pass)) {
                return false;
            }

            indexes.add(pass.getIndex());
        }

        return indexes.size() == 3;
    }

    private boolean shouldUnlock(UserData user) {
        var currentDate = new Date();

        if (currentDate.getTime() > user.getLockedUntil().getTime()) {
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
