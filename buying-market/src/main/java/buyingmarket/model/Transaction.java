package buyingmarket.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private Long id;
    @Column
    private LocalDateTime time;
    @Column
    private BigDecimal price;
    @Column
    private Long volume;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
