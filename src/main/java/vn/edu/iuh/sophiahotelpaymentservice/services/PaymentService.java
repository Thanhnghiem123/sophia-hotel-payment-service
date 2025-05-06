package vn.edu.iuh.sophiahotelpaymentservice.services;

import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentRequest;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentResponse;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest paymentRequest);
    PaymentResponse getPaymentById(UUID paymentId);
    List<PaymentResponse> getPaymentsByUserId(UUID userId);
    List<PaymentResponse> getAllPayments();
    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);
}
