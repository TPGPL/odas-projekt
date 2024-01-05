package pl.edu.pw.odasprojekt.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.pw.odasprojekt.model.domain.*;
import pl.edu.pw.odasprojekt.repositories.PaymentRepository;
import pl.edu.pw.odasprojekt.repositories.UserRepository;

import java.util.Date;
import java.util.List;

@Component
public class UserSeeder {
    private final UserRepository repository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserSeeder(UserRepository repository, PaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.paymentRepository = paymentRepository;
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

        var user2 = UserData.builder()
                .clientNumber("1231231231231")
                .secretHash(passwordEncoder.encode(991998 + "jttsmxnabc"))
                .secretSalt("jttsmxnabc")
                .build();

        user2.setBalance(UserBalance.builder()
                .balance(2137.11)
                .cvv("256")
                .cardNumber("1234123412341234")
                .expireAt(new Date())
                .user(user2)
                .build());

        user2.setPersonalData(UserPersonalData.builder()
                .user(user2)
                .email("jan@pawel.com")
                .name("Jan")
                .surname("Pawel")
                .PESEL("12121212333")
                .build());

        user2.getAuth().addAll(List.of(
                UserAuth.builder().index(1).secret(1664264).user(user2).build(),
                UserAuth.builder().index(2).secret(2501781).user(user2).build(),
                UserAuth.builder().index(3).secret(3504479).user(user2).build(),
                UserAuth.builder().index(4).secret(4672352).user(user2).build(),
                UserAuth.builder().index(5).secret(6005381).user(user2).build(),
                UserAuth.builder().index(6).secret(7503550).user(user2).build(),
                UserAuth.builder().index(7).secret(9166925).user(user2).build(),
                UserAuth.builder().index(8).secret(10995453).user(user2).build(),
                UserAuth.builder().index(9).secret(12989152).user(user2).build(),
                UserAuth.builder().index(10).secret(15148000).user(user2).build(),
                UserAuth.builder().index(11).secret(17472035).user(user2).build(),
                UserAuth.builder().index(12).secret(19961273).user(user2).build(),
                UserAuth.builder().index(13).secret(22615629).user(user2).build(),
                UserAuth.builder().index(14).secret(25435151).user(user2).build(),
                UserAuth.builder().index(15).secret(28419839).user(user2).build(),
                UserAuth.builder().index(16).secret(31569693).user(user2).build()));

        repository.save(user2);

        var currentDate = new Date();
        var payments = List.of(
                Payment.builder().recipient(user).sender(user2).title("Przelew numer 1").amount(12.22).sentAt(new Date(currentDate.getTime() + 10000)).build(),
                Payment.builder().recipient(user).sender(user2).title("Przelew numer 2").amount(21.67).sentAt(new Date(currentDate.getTime() + 20000)).build(),
                Payment.builder().recipient(user).sender(user2).title("Przelew numer 3").amount(33.42).sentAt(new Date(currentDate.getTime() + 30000)).build(),
                Payment.builder().recipient(user).sender(user2).title("Przelew numer 4").amount(10.31).sentAt(new Date(currentDate.getTime() + 40000)).build(),
                Payment.builder().recipient(user).sender(user2).title("Przelew numer 5").amount(9.88).sentAt(new Date(currentDate.getTime() + 50000)).build()
        );

        paymentRepository.saveAll(payments);
    }
}
