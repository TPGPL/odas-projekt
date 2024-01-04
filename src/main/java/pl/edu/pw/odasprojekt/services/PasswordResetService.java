package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.domain.PasswordResetToken;
import pl.edu.pw.odasprojekt.model.dtos.ForgetPasswordDto;
import pl.edu.pw.odasprojekt.repositories.PasswordResetTokenRepository;

import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final static int VALID_TIME = 30 * 60 * 1000;
    private final PasswordResetTokenRepository repository;
    private final UserService userService;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public void handleForgetRequest(ForgetPasswordDto dto) {
        // TODO: validate dto when jakarta validation is available

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
}
