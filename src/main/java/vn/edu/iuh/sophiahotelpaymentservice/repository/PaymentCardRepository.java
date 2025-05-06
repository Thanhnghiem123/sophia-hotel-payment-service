package vn.edu.iuh.sophiahotelpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.sophiahotelpaymentservice.model.PaymentCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, UUID> {
    List<PaymentCard> findByUserId(UUID userId);
    Optional<PaymentCard> findByUserIdAndIsDefaultTrue(UUID userId);
    Optional<PaymentCard> findByCardNumber(String cardNumber);
}
