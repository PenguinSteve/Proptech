package tdtu.Proptech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import tdtu.Proptech.dto.PaymentDTO;
import tdtu.Proptech.model.Payment;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.PaymentRepository;
import tdtu.Proptech.repository.UserRepository;
import tdtu.Proptech.request.AssignSubscriptionRequest;
import tdtu.Proptech.service.payment.PaymentServiceImpl;
import tdtu.Proptech.service.subscription.SubscriptionService;
import tdtu.Proptech.service.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignSubscription_NewSubscription_Success() {
        String userEmail = "test@domain.com";
        AssignSubscriptionRequest request = new AssignSubscriptionRequest(2L, 100.0, LocalDateTime.now(), "CREDIT_CARD");

        User user = new User();
        user.setSubscription(null);

        Subscription subscription = new Subscription();
        subscription.setId(2L);
        subscription.setDurationInDays(30);

        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(subscriptionService.getSubscriptionById(2L)).thenReturn(subscription);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Payment result = paymentService.assignSubscription(userEmail, request);

        assertNotNull(result);
        assertEquals(subscription, result.getSubscription());
        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(subscriptionService, times(1)).getSubscriptionById(2L);
        verify(userRepository, times(1)).save(user);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAssignSubscription_CurrentSubscriptionActive_HigherPriority() {
        String userEmail = "test@domain.com";
        AssignSubscriptionRequest request = new AssignSubscriptionRequest(3L, 150.0, LocalDateTime.now(), "PAYPAL");

        User user = new User();
        Subscription basic = new Subscription();
        basic.setId(2L);
        basic.setPlanName("BASIC");
        basic.setDurationInDays(30);
        basic.setPrice(600000.0);
        basic.setBenefits("Tối đa 10 tin (Hiển thị trong 14 ngày)");
        user.setSubscription(basic);
        user.setExpireSubscription(LocalDateTime.now().plusDays(basic.getDurationInDays()));

        Subscription currentSubscription = new Subscription();
        currentSubscription.setId(2L);

        Subscription newSubscription = new Subscription();
        newSubscription.setId(3L);
        newSubscription.setDurationInDays(60);

        user.setSubscription(currentSubscription);

        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(subscriptionService.getSubscriptionById(3L)).thenReturn(newSubscription);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Payment result = paymentService.assignSubscription(userEmail, request);

        assertNotNull(result);
        assertEquals(newSubscription, result.getSubscription());
        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(subscriptionService, times(1)).getSubscriptionById(3L);
        verify(userRepository, times(1)).save(user);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAssignSubscription_CurrentSubscriptionActive_LowerPriority() {
        String userEmail = "test@domain.com";
        AssignSubscriptionRequest request = new AssignSubscriptionRequest(1L, 50.0, LocalDateTime.now(), "CASH");

        User user = new User();
        Subscription basic = new Subscription();
        basic.setId(2L);
        basic.setPlanName("BASIC");
        basic.setDurationInDays(30);
        basic.setPrice(600000.0);
        basic.setBenefits("Tối đa 10 tin (Hiển thị trong 14 ngày)");
        user.setSubscription(basic);
        user.setExpireSubscription(LocalDateTime.now().plusDays(basic.getDurationInDays()));
        Subscription currentSubscription = new Subscription();
        currentSubscription.setId(2L);

        Subscription newSubscription = new Subscription();
        newSubscription.setId(1L);

        user.setSubscription(currentSubscription);

        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(subscriptionService.getSubscriptionById(1L)).thenReturn(newSubscription);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.assignSubscription(userEmail, request));

        assertEquals("Cannot assign new subscription because the current subscription has not ended.", exception.getMessage());
        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(subscriptionService, times(1)).getSubscriptionById(1L);
        verifyNoInteractions(paymentRepository);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetPayments() {
        Payment payment = new Payment();
        when(paymentRepository.findAll()).thenReturn(Collections.singletonList(payment));

        List<Payment> payments = paymentService.getPayments();

        assertEquals(1, payments.size());
        assertSame(payment, payments.get(0));
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testConvertPaymentToPaymentDTO() {
        Payment payment = new Payment();
        PaymentDTO paymentDTO = new PaymentDTO();

        when(modelMapper.map(payment, PaymentDTO.class)).thenReturn(paymentDTO);

        PaymentDTO result = paymentService.convertPaymentToPaymentDTO(payment);

        assertNotNull(result);
        assertSame(paymentDTO, result);
        verify(modelMapper, times(1)).map(payment, PaymentDTO.class);
    }

    @Test
    void testConvertPaymentsToPaymentsDTO() {
        Payment payment = new Payment();
        PaymentDTO paymentDTO = new PaymentDTO();

        when(modelMapper.map(payment, PaymentDTO.class)).thenReturn(paymentDTO);

        List<PaymentDTO> result = paymentService.convertPaymentsToPaymentsDTO(Collections.singletonList(payment));

        assertEquals(1, result.size());
        assertSame(paymentDTO, result.get(0));
        verify(modelMapper, times(1)).map(payment, PaymentDTO.class);
    }
}
