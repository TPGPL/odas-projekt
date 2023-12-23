package pl.edu.pw.odasprojekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.edu.pw.odasprojekt.seeders.UserSeeder;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserSeeder seeder;

    @Autowired
    public DataLoader(UserSeeder seeder) {
        this.seeder = seeder;
    }

    @Override
    public void run(String... args) {
        seeder.seed();
    }
}
