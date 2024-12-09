package tdtu.Proptech.service.payment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tdtu.Proptech.dto.PaymentDTO;
import tdtu.Proptech.model.Payment;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.PaymentRepository;
import tdtu.Proptech.repository.UserRepository;
import tdtu.Proptech.request.AssignSubscriptionRequest;
import tdtu.Proptech.service.subscription.SubscriptionService;
import tdtu.Proptech.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final UserRepository userRepository;

    private final UserService userService;

    private final PaymentRepository paymentRepository;

    private final SubscriptionService subscriptionService;

    private final ModelMapper modelMapper;

    @Override
    public Payment assignSubscription(String userEmail, AssignSubscriptionRequest request) {
        Subscription newSubscription = subscriptionService.getSubscriptionById(request.getSubscriptionId());
        User existingUser = userService.getUserByEmail(userEmail);

        if (existingUser.isSubscriptionActive()) {
            Subscription currentSubscription = existingUser.getSubscription();
            if (newSubscription.getId() > currentSubscription.getId()) {
                return createPayment(existingUser, newSubscription, request);
            } else {
                throw new RuntimeException("Cannot assign new subscription because the current subscription has not ended.");
            }
        } else {
            return createPayment(existingUser, newSubscription, request);
        }
    }

    private Payment createPayment(User existingUser, Subscription newSubscription, AssignSubscriptionRequest request) {
        Payment payment = new Payment();
        payment.setRealtor(existingUser);
        payment.setSubscription(newSubscription);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());

        existingUser.setSubscription(newSubscription);
        existingUser.setExpireSubscription(LocalDateTime.now().plusDays(newSubscription.getDurationInDays()));
        userRepository.save(existingUser);
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentDTO convertPaymentToPaymentDTO(Payment payment) {
        return modelMapper.map(payment, PaymentDTO.class);
    }

    @Override
    public List<PaymentDTO> convertPaymentsToPaymentsDTO(List<Payment> payments) {
        return payments.stream().map(this :: convertPaymentToPaymentDTO).toList();
    }
}