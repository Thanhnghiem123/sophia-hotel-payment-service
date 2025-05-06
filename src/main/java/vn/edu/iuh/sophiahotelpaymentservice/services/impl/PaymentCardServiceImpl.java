package vn.edu.iuh.sophiahotelpaymentservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardRequest;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardResponse;
import vn.edu.iuh.sophiahotelpaymentservice.exceptions.BadRequestException;
import vn.edu.iuh.sophiahotelpaymentservice.exceptions.ResourceNotFoundException;
import vn.edu.iuh.sophiahotelpaymentservice.model.PaymentCard;
import vn.edu.iuh.sophiahotelpaymentservice.repository.PaymentCardRepository;
import vn.edu.iuh.sophiahotelpaymentservice.services.PaymentCardService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentCardServiceImpl implements PaymentCardService {

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @Override
    public PaymentCardResponse addCard(PaymentCardRequest cardRequest) {
        // Kiểm tra xem thẻ đã tồn tại chưa
        if (paymentCardRepository.findByCardNumber(cardRequest.getCardNumber()).isPresent()) {
            throw new BadRequestException("Card with this number already exists");
        }

        PaymentCard card = new PaymentCard();
        card.setUserId(cardRequest.getUserId());
        card.setCardNumber(cardRequest.getCardNumber());
        card.setCardHolderName(cardRequest.getCardHolderName());
        card.setCardType(cardRequest.getCardType());
        card.setExpiryDate(cardRequest.getExpiryDate());
        card.setCvvHash(hashCVV(cardRequest.getCvv()));
        card.setIsDefault(cardRequest.getIsDefault());

        // Nếu thẻ này được đánh dấu là mặc định, cập nhật các thẻ khác
        if (Boolean.TRUE.equals(card.getIsDefault())) {
            updateDefaultCards(cardRequest.getUserId());
        }

        PaymentCard savedCard = paymentCardRepository.save(card);
        return convertToResponse(savedCard);
    }

    @Override
    public PaymentCardResponse getCardById(UUID cardId) {
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        return convertToResponse(card);
    }

    @Override
    public List<PaymentCardResponse> getCardsByUserId(UUID userId) {
        List<PaymentCard> cards = paymentCardRepository.findByUserId(userId);
        return cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentCardResponse getDefaultCard(UUID userId) {
        PaymentCard card = paymentCardRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No default card found for user: " + userId));
        return convertToResponse(card);
    }

    @Override
    @Transactional
    public PaymentCardResponse updateCard(UUID cardId, PaymentCardRequest cardRequest) {
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

        // Cập nhật thông tin thẻ
        card.setCardHolderName(cardRequest.getCardHolderName());
        card.setExpiryDate(cardRequest.getExpiryDate());
        
        // Nếu có thay đổi CVV
        if (cardRequest.getCvv() != null && !cardRequest.getCvv().isEmpty()) {
            card.setCvvHash(hashCVV(cardRequest.getCvv()));
        }

        // Nếu thẻ này được đánh dấu là mặc định, cập nhật các thẻ khác
        if (Boolean.TRUE.equals(cardRequest.getIsDefault()) && !Boolean.TRUE.equals(card.getIsDefault())) {
            updateDefaultCards(card.getUserId());
            card.setIsDefault(true);
        }

        PaymentCard updatedCard = paymentCardRepository.save(card);
        return convertToResponse(updatedCard);
    }

    @Override
    @Transactional
    public void deleteCard(UUID cardId) {
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));
        paymentCardRepository.delete(card);
    }

    @Override
    @Transactional
    public PaymentCardResponse setDefaultCard(UUID cardId) {
        PaymentCard card = paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

        // Cập nhật các thẻ khác
        updateDefaultCards(card.getUserId());

        // Đặt thẻ này là mặc định
        card.setIsDefault(true);
        PaymentCard updatedCard = paymentCardRepository.save(card);
        return convertToResponse(updatedCard);
    }

    private void updateDefaultCards(UUID userId) {
        List<PaymentCard> userCards = paymentCardRepository.findByUserId(userId);
        userCards.forEach(c -> {
            if (Boolean.TRUE.equals(c.getIsDefault())) {
                c.setIsDefault(false);
                paymentCardRepository.save(c);
            }
        });
    }

    private String hashCVV(String cvv) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(cvv.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing CVV", e);
        }
    }

    private PaymentCardResponse convertToResponse(PaymentCard card) {
        String maskedCardNumber = maskCardNumber(card.getCardNumber());
        
        return new PaymentCardResponse(
                card.getId(),
                card.getUserId(),
                maskedCardNumber,
                card.getCardHolderName(),
                card.getCardType(),
                card.getExpiryDate(),
                card.getIsDefault(),
                card.getCreatedAt()
        );
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        
        int length = cardNumber.length();
        String lastFourDigits = cardNumber.substring(length - 4);
        StringBuilder masked = new StringBuilder();
        
        for (int i = 0; i < length - 4; i++) {
            masked.append("*");
        }
        
        masked.append(lastFourDigits);
        return masked.toString();
    }
}
