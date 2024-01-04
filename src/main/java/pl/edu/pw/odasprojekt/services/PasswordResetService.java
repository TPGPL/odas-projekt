package pl.edu.pw.odasprojekt.services;

import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.ServiceResponse;
import pl.edu.pw.odasprojekt.model.domain.PasswordResetToken;
import pl.edu.pw.odasprojekt.model.dtos.ChangePasswordDto;
import pl.edu.pw.odasprojekt.model.dtos.ForgetPasswordDto;
import pl.edu.pw.odasprojekt.repositories.PasswordResetTokenRepository;

import java.util.Date;
import java.util.UUID;

import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.verifyPassword;
import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.verifyResetToken;

@Service
public class PasswordResetService {
    private final static int VALID_TIME = 30 * 60 * 1000;
    private final PasswordResetTokenRepository repository;
    private final UserService userService;
    private final AuthService authService;
    private final Validator validator;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository repository, UserService userService, AuthService authService, Validator validator) {
        this.repository = repository;
        this.userService = userService;
        this.authService = authService;
        this.validator = validator;
    }

    public void handleForgetRequest(ForgetPasswordDto dto) {
        if (!validateForgetDto(dto)) {
            return;
        }

        var user = userService.getUserByEmail(dto.getEmail());

        if (user == null) {
            return;
        }

        var createdDate = new Date();
        var validTo = new Date(createdDate.getTime() + VALID_TIME);
        var token = UUID.randomUUID();

        while (repository.existsByToken(token.toString())) {
            token = UUID.randomUUID(); // shouldn't happen but for safety
        }

        var resetToken = PasswordResetToken.builder()
                .createdAt(createdDate)
                .expireAt(validTo)
                .user(user)
                .token(token.toString())
                .build();

        repository.save(resetToken);

        System.out.printf("Wysyłam email na adres %s z linkiem URL/change-password?token=%s%n", dto.getEmail(), token);
    }

    public boolean verifyTokenValidity(String token) {
        if (!verifyResetToken(token) || !repository.existsByToken(token)) {
            return false;
        }

        var resetToken = repository.findByToken(token).orElse(null);
        var currentTime = new Date().getTime();

        assert resetToken != null;

        return resetToken.getExpireAt().getTime() > currentTime;
    }

    public ServiceResponse<Void> handleChangeRequest(ChangePasswordDto dto) {
        if (!validateChangeDto(dto)) {
            return ServiceResponse.<Void>builder().success(false).message("Wprowadzono nieprawidłowe dane!").build();
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ServiceResponse.<Void>builder().success(false).message("Podane hasła różnią się!").build();
        }

        if (!authService.verifyPasswordStrength(dto.getPassword())) {
            return ServiceResponse.<Void>builder().success(false).message("Podane hasło jest zbyt słabe!").build();
        }

        var bearerId = getTokenBearerId(dto.getToken());

        authService.changePassword(bearerId, dto.getPassword());

        invalidateToken(dto.getToken());

        return ServiceResponse.<Void>builder().success(true).build();
    }

    private int getTokenBearerId(String token) {
        var resetToken = repository.findByToken(token).orElse(null);

        return resetToken != null ? resetToken.getUser().getId() : -1;
    }

    private void invalidateToken(String token) {
        var resetToken = repository.findByToken(token).orElse(null);

        if (resetToken == null) {
            return;
        }

        resetToken.setExpireAt(new Date(0));
        repository.save(resetToken);
    }

    private boolean validateForgetDto(ForgetPasswordDto dto) {
        if (dto == null) {
            return false;
        }

        var violations = validator.validate(dto);

        return violations.isEmpty();
    }

    private boolean validateChangeDto(ChangePasswordDto dto) {
        return dto != null && verifyTokenValidity(dto.getToken()) && verifyPassword(dto.getPassword()) && verifyPassword(dto.getConfirmPassword());
    }
}
