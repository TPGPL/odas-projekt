package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.edu.pw.odasprojekt.model.domain.UserAuth;
import pl.edu.pw.odasprojekt.model.dtos.PasswordFragmentDto;
import pl.edu.pw.odasprojekt.repositories.AuthRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private final static int PASS_LENGTH = 16;
    private final static int MAX_SECRET_VALUE = 1000000;
    private final AuthRepository authRepository;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public long calculateLoginSecret(int userId, PasswordFragmentDto[] passFrags) {
        long[] indices = {passFrags[0].getIndex() + 1, passFrags[1].getIndex() + 1, passFrags[2].getIndex() + 1};
        HashMap<Long, UserAuth> secrets = new HashMap<>();

        for (long id : indices) {
            secrets.put(id, authRepository.findByIndexAndUserId((int) id, userId).orElse(null));
        }

        // ...
        long i = indices[0], j = indices[1], k = indices[2];
        long secret1 = secrets.get(i).getSecret() + passFrags[0].getValue();
        long secret2 = secrets.get(j).getSecret() + passFrags[1].getValue();
        long secret3 = secrets.get(k).getSecret() + passFrags[2].getValue();

        long top = (j * k * secret1) * (j - k) - (i * k * secret2) * (i - k) + (i * j * secret3) * (i - j);
        long bottom = (i - j) * (i - k) * (j - k);

        return top / bottom;
    }

    public boolean verifyPasswordStrength(String password) {
        var smallLetters = Pattern.compile("[a-z]");
        var bigLetters = Pattern.compile("[A-Z]");
        var numbers = Pattern.compile("[0-9]");
        var special = Pattern.compile("[@!%$#^&*+=_ ]");

        if (!smallLetters.matcher(password).find() || !bigLetters.matcher(password).find()
                || !numbers.matcher(password).find() || !special.matcher(password).find()) {
            return false;
        }

        double entropy = 0;
        var chars = new ArrayList<Character>();

        for (var ch : password.toCharArray()) {
            if (!chars.contains(ch)) {
                chars.add(ch);
            }
        }

        for (var ch : chars) {
            entropy += calculateCharEntropy(password, ch);
        }

        entropy *= -1;

        return entropy > 3.5;
    }

    public int changePassword(int userId, String password) {
        var secrets = authRepository.findAllByUserId(userId);

        var rand = new Random();

        int K = rand.nextInt(MAX_SECRET_VALUE);
        int R1 = rand.nextInt(MAX_SECRET_VALUE);
        int R2 = rand.nextInt(MAX_SECRET_VALUE);

        var newSecrets = new HashMap<Integer, Integer>();

        for (int i = 1; i <= password.length(); i++) {
            int s = (K + R1 * i + R2 * i * i) - (password.charAt(i - 1));
            newSecrets.put(i, s);
        }

        for (var secret : secrets) {
            secret.setSecret(newSecrets.get(secret.getIndex()));
        }

        authRepository.saveAll(secrets);

        return K;
    }

    private double calculateCharEntropy(String password, char ch) {
        int count = StringUtils.countOccurrencesOf(password, Character.toString(ch));

        double prob = count / (double) PASS_LENGTH;

        return (Math.log(prob) / Math.log(2)) * prob;
    }
}
