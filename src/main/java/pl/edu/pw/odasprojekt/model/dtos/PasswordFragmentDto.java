package pl.edu.pw.odasprojekt.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordFragmentDto {
    /**
     * Indeks znaku hasła (liczony od 0...password.length-1)
     */
    int index;

    /**
     * (index+1)-ty znak hasła.
     */
    char value;
}
