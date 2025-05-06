package vn.edu.iuh.sophiahotelpaymentservice.services;

import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardRequest;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentCardService {
    PaymentCardResponse addCard(PaymentCardRequest cardRequest);
    PaymentCardResponse getCardById(UUID cardId);
    List<PaymentCardResponse> getCardsByUserId(UUID userId);
    PaymentCardResponse getDefaultCard(UUID userId);
    PaymentCardResponse updateCard(UUID cardId, PaymentCardRequest cardRequest);
    void deleteCard(UUID cardId);
    PaymentCardResponse setDefaultCard(UUID cardId);
}
