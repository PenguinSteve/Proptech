package tdtu.Proptech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private User realtor;
    private Subscription subscription;
    private Double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
}
