package buyingMarket.services;

import buyingMarket.model.Order;
import buyingMarket.model.Position;
import buyingMarket.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public void startPosition(Order order, BigDecimal margin) {
        //TODO check margin requirements, deduct from balance,...
        Position position = Position.builder().margin(margin).orders(Set.of(order)).user(order.getUser()).build();
        positionRepository.save(position);
    }

    public void closePosition(Long id, Order order) {
        //TODO calculate pnl, add to user balance,...
        Position position = positionRepository.findById(id).orElseThrow();
        position.getOrders().add(order);
        positionRepository.save(position);
    }
}
