package vn.edu.iuh.sophiahotelpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.sophiahotelpaymentservice.enums.CardType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCardRequest {
    private UUID userId;
    private String cardNumber;
    private String cardHolderName;
    private CardType cardType;
    private String expiryDate;
    private String cvv;
    private Boolean isDefault;
}
