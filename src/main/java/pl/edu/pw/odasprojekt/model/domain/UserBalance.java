package pl.edu.pw.odasprojekt.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="UserBalance")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBalance {
    @Id
    @GeneratedValue
    private int id;
    private String cardNumber;
    private String cvv;
    private Date expireAt;
    private double balance;
    @OneToOne(mappedBy = "balance")
    private UserData user;
}
