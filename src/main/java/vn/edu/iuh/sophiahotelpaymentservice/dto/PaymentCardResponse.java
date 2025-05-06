package vn.edu.iuh.sophiahotelpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.sophiahotelpaymentservice.enums.CardType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCardResponse {
    private UUID id;
    private UUID userId;
    private String maskedCardNumber;
    private String cardHolderName;
    private CardType cardType;
    private String expiryDate;
    private Boolean isDefault;
    private LocalDateTime createdAt;
}
