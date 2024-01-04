package pl.edu.pw.odasprojekt.services;

import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.domain.PasswordResetToken;
import pl.edu.pw.odasprojekt.model.dtos.ForgetPasswordDto;
import pl.edu.pw.odasprojekt.repositories.PasswordResetTokenRepository;

import java.util.Date;
import java.util.UUID;

import static pl.edu.pw.odasprojekt.utils.ValidatorUtils.verifyResetToken;

@Service
public class PasswordResetService {
    private final static int VALID_TIME = 30 * 60 * 1000;
    private final PasswordResetTokenRepository repository;
    private final UserService userService;
    private final Validator validator;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository repository, UserService userService, Validator validator) {
        this.repository = repository;
        this.userService = userService;
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

    private boolean validateForgetDto(ForgetPasswordDto dto) {
        if (dto == null) {
            return false;
        }

        var violations = validator.validate(dto);

        return violations.isEmpty();
    }
}
