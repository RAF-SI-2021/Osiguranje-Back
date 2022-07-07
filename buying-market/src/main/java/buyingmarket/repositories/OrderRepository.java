package buyingmarket.repositories;

import buyingmarket.model.Actuary;
import buyingmarket.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByActuary(Actuary actuary);
//    List<Order> findAllByActuaryAndActive(Actuary actuary, Boolean active);
    Optional<Order> findByOrderIdAndActuary(Long id, Actuary actuary);
}
