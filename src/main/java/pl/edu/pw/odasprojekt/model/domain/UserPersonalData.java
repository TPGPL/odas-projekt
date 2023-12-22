package pl.edu.pw.odasprojekt.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserPersonalData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPersonalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String PESEL;
    @OneToOne(mappedBy = "personalData")
    private UserData user;
}
