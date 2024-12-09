package tdtu.Proptech.dto;

import lombok.Data;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private User realtor;
    private Subscription subscription;
    private Double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
}
