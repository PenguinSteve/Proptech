package tdtu.Proptech.service.payment;

import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;

public interface PaymentService {
    User assignSubscription(User user, Subscription subscription);
}
