package tdtu.Proptech.service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User assignSubscription(User user, Subscription subscription) {
        user.setSubscription(subscription);
        LocalDateTime startDate = LocalDateTime.now();
        user.setStartDate(startDate);

        LocalDateTime endDate = startDate.plusDays(subscription.getDurationInDays());
        user.setEndDate(endDate);

        return userRepository.save(user);
    }
}
