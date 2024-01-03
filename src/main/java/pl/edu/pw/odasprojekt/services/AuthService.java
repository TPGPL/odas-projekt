package pl.edu.pw.odasprojekt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.odasprojekt.model.domain.UserAuth;
import pl.edu.pw.odasprojekt.model.dtos.PasswordFragmentDto;
import pl.edu.pw.odasprojekt.repositories.AuthRepository;

import java.util.HashMap;

@Service
public class AuthService {
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
}
