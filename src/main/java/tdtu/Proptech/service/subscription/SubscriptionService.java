package tdtu.Proptech.service.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import tdtu.Proptech.model.Subscription;

import java.util.List;

public interface SubscriptionService{
    Subscription getSubscriptionById(Long id);
    Subscription getSubscriptionsByPlanName(String name);
    List<Subscription> getAllSubscription();
}
