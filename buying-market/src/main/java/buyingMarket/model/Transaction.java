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
    private long receiptId;
    @Column
    private LocalDateTime time;
    @Column
    private String security;
    @Column
    private BigDecimal price;
    @Column
    private Long volume;

    public Transaction(long receiptId, LocalDateTime time, String security, BigDecimal price, Long volume) {
        this.receiptId = receiptId;
        this.time = time;
        this.security = security;
        this.price = price;
        this.volume = volume;
    }
}
