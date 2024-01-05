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

    public double getUserBalance(String clientNumber) {
        var balance = repository.findByUserClientNumber(clientNumber).orElse(null);

        return balance != null ? balance.getBalance() : 0;
    }

    public String getUserCardNumber(String clientNumber) {
        var balance = repository.findByUserClientNumber(clientNumber).orElse(null);

        return balance != null ? balance.getCardNumber() : null;
    }

    public void adjustUserBalance(String clientNumber, double amount) {
        var userBalance = repository.findByUserClientNumber(clientNumber).orElse(null);

        if (userBalance == null) {
            return;
        }

        var currentBalance = userBalance.getBalance();

        userBalance.setBalance(currentBalance + amount);

        repository.save(userBalance);
    }
}
