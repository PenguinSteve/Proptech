package tdtu.Proptech.service.payment;

import tdtu.Proptech.dto.PaymentDTO;
import tdtu.Proptech.model.Payment;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.request.AssignSubscriptionRequest;

import java.util.List;

public interface PaymentService {
    Payment assignSubscription(String userEmail, AssignSubscriptionRequest request);
    List<Payment> getPayments();

    PaymentDTO convertPaymentToPaymentDTO(Payment payment);
    List<PaymentDTO> convertPaymentsToPaymentsDTO(List<Payment> payments);
}
