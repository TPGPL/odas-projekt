package pl.edu.pw.odasprojekt.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordDto {
    // TODO: Add validation package and annotations here
    private String email;
}
