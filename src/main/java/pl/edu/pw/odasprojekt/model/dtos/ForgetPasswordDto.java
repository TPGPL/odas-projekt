package pl.edu.pw.odasprojekt.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class ForgetPasswordDto {
    @Email
    @NotNull
    @Size(min = 1, max = 99)
    private String email;
}
