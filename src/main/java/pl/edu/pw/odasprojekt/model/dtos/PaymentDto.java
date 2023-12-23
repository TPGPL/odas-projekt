package pl.edu.pw.odasprojekt.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PaymentDto {
    private String title;
    private Date sentAt;
    private double amount;
    private String recipientNumber;
}
