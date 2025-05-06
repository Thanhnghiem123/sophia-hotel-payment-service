package vn.edu.iuh.sophiahotelpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentMethod;
import vn.edu.iuh.sophiahotelpaymentservice.enums.PaymentStatus;
import vn.edu.iuh.sophiahotelpaymentservice.model.Payment;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByUserId(UUID userId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPaymentMethod(PaymentMethod method);
    List<Payment> findByUserIdAndStatus(UUID userId, PaymentStatus status);
}
