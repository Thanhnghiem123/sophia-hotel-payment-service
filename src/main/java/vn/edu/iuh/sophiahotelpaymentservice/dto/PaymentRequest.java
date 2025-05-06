package vn.edu.iuh.sophiahotelpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentMethod;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private UUID userId;
    private String currency;
    private Double amount;
    private PaymentMethod paymentMethod;
    private UUID cardId; // ID của thẻ nếu thanh toán bằng thẻ
}
