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

    public int calculateLoginSecret(int userId, PasswordFragmentDto[] passFrags) {
        int[] indices = {passFrags[0].getIndex() + 1, passFrags[1].getIndex() + 1, passFrags[2].getIndex() + 1};
        HashMap<Integer, UserAuth> secrets = new HashMap<>();

        for (int id : indices) {
            secrets.put(id, authRepository.findByIndexAndUserId(id, userId).orElse(null));
        }

        int secret1 = secrets.get(indices[0]).getSecret() + passFrags[0].getValue();
        int secret2 = secrets.get(indices[1]).getSecret() + passFrags[1].getValue();
        int secret3 = secrets.get(indices[2]).getSecret() + passFrags[2].getValue();

        return (indices[1] * indices[2] * secret1) / ((indices[0] - indices[1]) * (indices[0] - indices[2]))
                + (indices[0] * indices[2] * secret2) / ((indices[1] - indices[0]) * (indices[1] - indices[2]))
                + (indices[0] * indices[1] * secret3) / ((indices[2] - indices[1]) * (indices[2] - indices[0]));
        // TODO: works most of the time because of rounding, figure out if it's possible to reach 100% reliability with this solution
        //  https://www.wolframalpha.com/input?i=%288*11*1525537%29+%2F+%28%281-8%29*%281-11%29%29+%2B+%281*11*26862394%29+%2F+%28%288-1%29*%288-11%29%29+%2B+%281*8*49094197%29+%2F+%28%2811-1%29*%2811-8%29%29
    }
}
