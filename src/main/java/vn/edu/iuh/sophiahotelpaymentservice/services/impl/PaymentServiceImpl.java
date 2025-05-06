package vn.edu.iuh.sophiahotelpaymentservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardResponse;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentRequest;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentResponse;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentMethod;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentStatus;
import vn.edu.iuh.sophiahotelpaymentservice.exceptions.BadRequestException;
import vn.edu.iuh.sophiahotelpaymentservice.exceptions.ResourceNotFoundException;
import vn.edu.iuh.sophiahotelpaymentservice.model.Payment;
import vn.edu.iuh.sophiahotelpaymentservice.model.PaymentCard;
import vn.edu.iuh.sophiahotelpaymentservice.repository.PaymentCardRepository;
import vn.edu.iuh.sophiahotelpaymentservice.repository.PaymentRepository;
import vn.edu.iuh.sophiahotelpaymentservice.services.PaymentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PaymentCardRepository paymentCardRepository;
    
    @Autowired
    private PaymentCardServiceImpl paymentCardService;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        // Kiểm tra thông tin thanh toán
        validatePaymentRequest(paymentRequest);
        
        // Tạo đối tượng Payment
        Payment payment = new Payment();
        payment.setUserId(paymentRequest.getUserId());
        payment.setCurrency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "USD");
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());
        
        // Xử lý thanh toán với cổng thanh toán
        boolean paymentSuccessful = processPaymentWithGateway(paymentRequest);
        
        if (paymentSuccessful) {
            payment.setStatus(PaymentStatus.COMPLETED);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        Payment savedPayment = paymentRepository.save(payment);
        return convertToPaymentResponse(savedPayment, paymentRequest.getCardId());
    }

    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        return convertToPaymentResponse(payment, null);
    }

    @Override
    public List<PaymentResponse> getPaymentsByUserId(UUID userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(payment -> convertToPaymentResponse(payment, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(payment -> convertToPaymentResponse(payment, null))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream()
                .map(payment -> convertToPaymentResponse(payment, null))
                .collect(Collectors.toList());
    }
    
    private void validatePaymentRequest(PaymentRequest request) {
        if (request.getUserId() == null) {
            throw new BadRequestException("User ID is required");
        }
        

        
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
        
        if (request.getPaymentMethod() == null) {
            throw new BadRequestException("Payment method is required");
        }
        
        // Kiểm tra thông tin thẻ nếu thanh toán bằng thẻ
        if ((request.getPaymentMethod() == PaymentMethod.CREDIT_CARD || 
             request.getPaymentMethod() == PaymentMethod.DEBIT_CARD) && 
            request.getCardId() == null) {
            throw new BadRequestException("Card ID is required for card payments");
        }
        
        // Kiểm tra xem thẻ có tồn tại không - tạm thời comment để test
        /*
        if (request.getCardId() != null) {
            paymentCardRepository.findById(request.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + request.getCardId()));
        }
        */
        // Chỉ log thông tin thẻ để debug
        if (request.getCardId() != null) {
            System.out.println("Using card ID: " + request.getCardId() + " (validation bypassed for testing)");
        }
    }
    
    private boolean processPaymentWithGateway(PaymentRequest paymentRequest) {
        // Trong ứng dụng thực tế, đây sẽ là tích hợp với cổng thanh toán
        // Hiện tại, chúng ta sẽ giả lập thanh toán thành công
        return true;
    }
    
    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
    
    private PaymentResponse convertToPaymentResponse(Payment payment, UUID cardId) {
        PaymentCardResponse cardResponse = null;
        
        // Nếu thanh toán bằng thẻ, lấy thông tin thẻ
        if ((payment.getPaymentMethod() == PaymentMethod.CREDIT_CARD || 
             payment.getPaymentMethod() == PaymentMethod.DEBIT_CARD) && 
            cardId != null) {
            try {
                cardResponse = paymentCardService.getCardById(cardId);
            } catch (Exception e) {
                // Xử lý nếu không tìm thấy thẻ
            }
        }
        
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
}
