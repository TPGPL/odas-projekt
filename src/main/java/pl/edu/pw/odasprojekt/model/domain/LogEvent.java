package pl.edu.pw.odasprojekt.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "LogEvent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date createdAt;
    @Enumerated(value = EnumType.STRING)
    private EventType event;
    private String address;
    private String details;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserData user;
}
