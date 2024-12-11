package tdtu.Proptech.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignSubscriptionRequest {
    private Long subscriptionId;
    private LocalDateTime paymentDate;
    private String paymentMethod;
}
