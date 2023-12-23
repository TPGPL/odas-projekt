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

        // TODO: Fix, doesn't always work
        return (secrets.get(indices[0]).getSecret() + passFrags[0].getValue()) * ((indices[1] * indices[2]) / ((indices[0] - indices[1]) * (indices[0] - indices[2])))
                + (secrets.get(indices[1]).getSecret() + passFrags[1].getValue()) * ((indices[0] * indices[2]) / ((indices[1] - indices[0]) * (indices[1] - indices[2])))
                + (secrets.get(indices[2]).getSecret() + passFrags[2].getValue()) * ((indices[0] * indices[1]) / ((indices[2] - indices[1]) * (indices[2] - indices[0])));
    }
}
