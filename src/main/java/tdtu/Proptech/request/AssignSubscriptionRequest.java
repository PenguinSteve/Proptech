package tdtu.Proptech.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignSubscriptionRequest {
    private Long subscriptionId;
    private Double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
}
