package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.repositories.BalanceRepository;

@Service
public class BalanceService {
    private final BalanceRepository repository;

    @Autowired
    public BalanceService(BalanceRepository repository) {
        this.repository = repository;
    }

    public double getUserBalance(String userNumber) {
        var balance = repository.findByUserClientNumber(userNumber).orElse(null);

        return balance != null ? balance.getBalance() : 0;
    }

    public void adjustUserBalance(String userNumber, double amount) {
        var userBalance = repository.findByUserClientNumber(userNumber).orElse(null);

        if (userBalance == null) {
            return;
        }

        var currentBalance = userBalance.getBalance();

        userBalance.setBalance(currentBalance + amount);

        repository.save(userBalance);
    }
}
