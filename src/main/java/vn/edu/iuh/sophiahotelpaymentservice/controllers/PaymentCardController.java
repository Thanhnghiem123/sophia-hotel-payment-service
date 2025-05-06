package vn.edu.iuh.sophiahotelpaymentservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardRequest;
import vn.edu.iuh.sophiahotelpaymentservice.dto.PaymentCardResponse;
import vn.edu.iuh.sophiahotelpaymentservice.services.PaymentCardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payment-cards")
public class PaymentCardController {

    @Autowired
    private PaymentCardService paymentCardService;

    @PostMapping
    public ResponseEntity<PaymentCardResponse> addCard(@RequestBody PaymentCardRequest cardRequest) {
        PaymentCardResponse cardResponse = paymentCardService.addCard(cardRequest);
        return new ResponseEntity<>(cardResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardResponse> getCardById(@PathVariable UUID id) {
        PaymentCardResponse cardResponse = paymentCardService.getCardById(id);
        return ResponseEntity.ok(cardResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentCardResponse>> getCardsByUserId(@PathVariable UUID userId) {
        List<PaymentCardResponse> cards = paymentCardService.getCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<PaymentCardResponse> getDefaultCard(@PathVariable UUID userId) {
        PaymentCardResponse cardResponse = paymentCardService.getDefaultCard(userId);
        return ResponseEntity.ok(cardResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentCardResponse> updateCard(
            @PathVariable UUID id,
            @RequestBody PaymentCardRequest cardRequest) {
        PaymentCardResponse cardResponse = paymentCardService.updateCard(id, cardRequest);
        return ResponseEntity.ok(cardResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        paymentCardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<PaymentCardResponse> setDefaultCard(@PathVariable UUID id) {
        PaymentCardResponse cardResponse = paymentCardService.setDefaultCard(id);
        return ResponseEntity.ok(cardResponse);
    }
}
