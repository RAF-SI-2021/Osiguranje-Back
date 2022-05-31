package buyingmarket.repositories;

import buyingmarket.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
    List<Order> findAllByUserIdAndActive(Long userId, Boolean active);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
