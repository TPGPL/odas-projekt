package pl.edu.pw.odasprojekt.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Random;

@Data
@Builder
public class UserLoginDto {
    private final static int MAX_PASSWORD_LENGTH = 16;
    private String clientNumber;
    @Builder.Default
    private PasswordFragmentDto[] passwordFrags = new PasswordFragmentDto[3];

    public void selectPasswordFragments() {
        var rand = new Random();
        var passInd = new HashSet<Integer>();

        while (passInd.size() < 3) {
            passInd.add(rand.nextInt(MAX_PASSWORD_LENGTH));
        }

        int i = 0;

        for (var ind : passInd) {
            passwordFrags[i] = PasswordFragmentDto.builder().index(ind).build();
            i++;
        }
    }
}
