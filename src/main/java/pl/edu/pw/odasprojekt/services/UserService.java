package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.ServiceResponse;
import pl.edu.pw.odasprojekt.model.domain.UserData;
import pl.edu.pw.odasprojekt.model.dtos.PasswordFragmentDto;
import pl.edu.pw.odasprojekt.model.dtos.UserLoginDto;
import pl.edu.pw.odasprojekt.repositories.UserRepository;

import java.util.regex.Pattern;

@Service
public class UserService {
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

    public ServiceResponse<String> login(UserLoginDto dto) {
        if (dto == null || !verifyClientNumber(dto.getClientNumber()) || !verifyPasswordFragments(dto.getPasswordFrags())) {
            return ServiceResponse.<String>builder().success(false).build();
        }

        var user = getUserByClientNumber(dto.getClientNumber());
        long calculatedSecret = authService.calculateLoginSecret(user.getId(), dto.getPasswordFrags());

        String givenPassword = calculatedSecret + user.getSecretSalt();

        if (passwordEncoder.matches(givenPassword, user.getSecretHash())) {
            return ServiceResponse.<String>builder().success(true).data(jwtService.generateJwtForUser(dto.getClientNumber())).build();
        }

        return ServiceResponse.<String>builder().success(false).build();
    }

    public void adjustBalance(String clientNumber, double amount) {
        var user = getUserByClientNumber(clientNumber);
        var currentBalance = user.getBalance().getBalance();

        user.getBalance().setBalance(currentBalance + amount);

        userRepository.save(user);
    }

    private boolean verifyPasswordFragments(PasswordFragmentDto[] passwordFrags) {
        if (passwordFrags == null) {
            return false;
        }

        var pattern = Pattern.compile("[0-9a-zA-Z@!$%]");

        for (var frag : passwordFrags) {
            if (frag == null)
                return false;

            if (frag.getIndex() < 0 || frag.getIndex() > 15)
                return false;

            if (!pattern.matcher(String.valueOf(frag.getValue())).find()) {
                return false;
            }
        }

        return true;
    }

    private boolean verifyClientNumber(String clientNumber) {
        if (clientNumber == null || clientNumber.length() != 13) {
            return false;
        }

        if (!Pattern.compile("[0-9]{13}").matcher(clientNumber).find()) {
            return false;
        }

        return userRepository.existsByClientNumber(clientNumber);
    }
}
