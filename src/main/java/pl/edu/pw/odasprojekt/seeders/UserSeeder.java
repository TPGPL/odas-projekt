package pl.edu.pw.odasprojekt.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.pw.odasprojekt.model.domain.UserAuth;
import pl.edu.pw.odasprojekt.model.domain.UserBalance;
import pl.edu.pw.odasprojekt.model.domain.UserData;
import pl.edu.pw.odasprojekt.model.domain.UserPersonalData;
import pl.edu.pw.odasprojekt.repositories.UserRepository;

import java.util.Date;
import java.util.List;

@Component
public class UserSeeder {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserSeeder(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seed() {
        var user = UserData.builder()
                .clientNumber("1112223334445")
                .secretHash(passwordEncoder.encode(938826 + "jttsmxnqvv"))
                .secretSalt("jttsmxnqvv")
                .build();
        user.setBalance(UserBalance.builder()
                .balance(111.22)
                .cvv("123")
                .cardNumber("1111222233334444")
                .expireAt(new Date())
                .user(user)
                .build());

        user.setPersonalData(UserPersonalData.builder()
                .user(user)
                .email("jaroslaw@kaczynski.com")
                .name("Jaroslaw")
                .surname("Kaczynski")
                .PESEL("12121212121")
                .build());

        user.getAuth().addAll(List.of(
                UserAuth.builder().index(1).secret(1525439).user(user).build(),
                UserAuth.builder().index(2).secret(2870347).user(user).build(),
                UserAuth.builder().index(3).secret(4973480).user(user).build(),
                UserAuth.builder().index(4).secret(7834832).user(user).build(),
                UserAuth.builder().index(5).secret(11454384).user(user).build(),
                UserAuth.builder().index(6).secret(15832120).user(user).build(),
                UserAuth.builder().index(7).secret(20968106).user(user).build(),
                UserAuth.builder().index(8).secret(26862289).user(user).build(),
                UserAuth.builder().index(9).secret(33514687).user(user).build(),
                UserAuth.builder().index(10).secret(40925278).user(user).build(),
                UserAuth.builder().index(11).secret(49094100).user(user).build(),
                UserAuth.builder().index(12).secret(58021169).user(user).build(),
                UserAuth.builder().index(13).secret(67706399).user(user).build(),
                UserAuth.builder().index(14).secret(78149839).user(user).build(),
                UserAuth.builder().index(15).secret(89351489).user(user).build(),
                UserAuth.builder().index(16).secret(101311349).user(user).build()));

        repository.save(user);
    }
}
