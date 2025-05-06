package vn.edu.iuh.sophiahotelpaymentservice.mappers;

import org.springframework.stereotype.Component;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardResponse;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentRequest;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentResponse;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentMethod;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentStatus;
import vn.edu.iuh.sophiahotelpaymentservice.model.Payment;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setUserId(paymentRequest.getUserId());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());
        return payment;
    }

    public PaymentResponse toDto(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),
                payment.getCurrency(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getCreatedAt(),
                null // PaymentCardResponse
        );
    }
    
    public PaymentResponse toDto(Payment payment, PaymentCardResponse cardResponse) {
        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),
                payment.getCurrency(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getCreatedAt(),
                cardResponse
        );
    }
    
    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
