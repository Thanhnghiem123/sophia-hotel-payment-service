package vn.edu.iuh.sophiahotelpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentMethod;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private UUID id;
    private UUID userId;
    private String currency;
    private Double amount;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private LocalDateTime createdAt;
    private PaymentCardResponse card; // Thông tin thẻ nếu thanh toán bằng thẻ
}
