package tdtu.Proptech.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.repository.SubscriptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Subscription not found!"));
    }

    @Override
    public Subscription getSubscriptionsByPlanName(String name) {
        return subscriptionRepository.findByPlanName(name).orElseThrow(() -> new RuntimeException("Subscription not found!"));
    }

    @Override
    public List<Subscription> getAllSubscription() {
        return subscriptionRepository.findAll();
    }
}
