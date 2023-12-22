package pl.edu.pw.odasprojekt.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "UserData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String clientNumber;
    private String secretHash;
    private String secretSalt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_id", referencedColumnName = "id")
    private UserPersonalData personalData;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id", referencedColumnName = "id")
    private UserBalance balance;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAuth> auth;
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
