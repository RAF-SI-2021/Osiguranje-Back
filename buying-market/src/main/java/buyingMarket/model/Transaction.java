package buyingMarket.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;
    @Column
    private long userId;
    @Column
    private LocalDateTime time;
    @Column
    private String security;
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal volume;
    @Column
    private OrderType orderType;

    public Transaction(long userId, LocalDateTime time, String security, BigDecimal price, BigDecimal volume, OrderType orderType) {
        this.userId = userId;
        this.time = time;
        this.security = security;
        this.price = price;
        this.volume = volume;
        this.orderType = orderType;
    }
}