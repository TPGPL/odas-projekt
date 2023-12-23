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
                .secretHash(passwordEncoder.encode(-1569089644 + "jttsmxnqvv"))
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
                UserAuth.builder().index(1).secret(758083680).user(user).build(),
                UserAuth.builder().index(2).secret(-1203161083).user(user).build(),
                UserAuth.builder().index(3).secret(1137110589).user(user).build(),
                UserAuth.builder().index(4).secret(-811035902).user(user).build(),
                UserAuth.builder().index(5).secret(1542334017).user(user).build(),
                UserAuth.builder().index(6).secret(-392714262).user(user).build(),
                UserAuth.builder().index(7).secret(1973753919).user(user).build(),
                UserAuth.builder().index(8).secret(51803915).user(user).build(),
                UserAuth.builder().index(9).secret(-1863596960).user(user).build(),
                UserAuth.builder().index(10).secret(522518568).user(user).build(),
                UserAuth.builder().index(11).secret(-1379784055).user(user).build(),
                UserAuth.builder().index(12).secret(1019429779).user(user).build(),
                UserAuth.builder().index(13).secret(-869774608).user(user).build(),
                UserAuth.builder().index(14).secret(1542537425).user(user).build(),
                UserAuth.builder().index(15).secret(-333568714).user(user).build(),
                UserAuth.builder().index(16).secret(2091841567).user(user).build()));

        repository.save(user);
    }
}
