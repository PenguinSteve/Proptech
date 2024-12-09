package tdtu.Proptech.controller.RestAPIController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tdtu.Proptech.model.Payment;
import tdtu.Proptech.request.AssignSubscriptionRequest;
import tdtu.Proptech.response.ApiResponse;
import tdtu.Proptech.service.payment.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class ApiPaymentController {

    private final PaymentService paymentService;

    @PostMapping("/assign-subscription")
    @PreAuthorize("hasRole('REALTOR')")
    public ResponseEntity<ApiResponse> assignSubscription(@Valid @RequestBody AssignSubscriptionRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();

            Payment payment = paymentService.assignSubscription(currentEmail, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Subscription assigned successfully", paymentService.convertPaymentToPaymentDTO(payment)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getPayments() {
        return ResponseEntity.ok(new ApiResponse("Payments retrieved successfully", paymentService.convertPaymentsToPaymentsDTO(paymentService.getPayments())));
    }
}
