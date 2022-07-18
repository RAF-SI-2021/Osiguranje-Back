package buyingmarket.repositories;

import buyingmarket.model.Actuary;
import buyingmarket.model.Order;
import buyingmarket.model.SecurityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByActuary(Actuary actuary);
    Optional<Order> findByOrderIdAndActuary(Long id, Actuary actuary);
    List<Order> findAllBySecurityIdAndSecurityType(Long securityID, SecurityType securityType);
}
