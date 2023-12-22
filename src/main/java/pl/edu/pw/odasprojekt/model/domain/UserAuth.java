package pl.edu.pw.odasprojekt.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserAuth")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int index;
    private int secret;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserData user;
}
