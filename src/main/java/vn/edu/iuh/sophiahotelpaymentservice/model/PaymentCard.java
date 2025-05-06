package vn.edu.iuh.sophiahotelpaymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.sophiahotelpaymentservice.enums.CardType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "card_number", nullable = false)
    private String cardNumber;
    
    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;
    
    @Column(name = "card_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    
    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;
    
    @Column(name = "cvv_hash")
    private String cvvHash;
    
    @Column(name = "is_default")
    private Boolean isDefault;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }
    

}
