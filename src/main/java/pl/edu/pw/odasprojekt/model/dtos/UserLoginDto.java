package pl.edu.pw.odasprojekt.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    private final static int MAX_PASSWORD_LENGTH = 16;
    private String clientNumber;
    @Builder.Default
    private PasswordFragmentDto s1 = new PasswordFragmentDto();
    @Builder.Default
    private PasswordFragmentDto s2 = new PasswordFragmentDto();
    @Builder.Default
    private PasswordFragmentDto s3 = new PasswordFragmentDto();

    public void selectPasswordFragments() {
        var rand = new Random();
        var passInd = new HashSet<Integer>();

        while (passInd.size() < 3) {
            passInd.add(rand.nextInt(MAX_PASSWORD_LENGTH));
        }

        int i = 0;

        var passwordFrags = List.of(s1,s2,s3);

        for (var ind : passInd) {
            passwordFrags.get(i).setIndex(ind);
            i++;
        }
    }
}
