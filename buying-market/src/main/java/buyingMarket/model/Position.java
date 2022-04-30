package buyingMarket.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal margin;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Order> orders;
}
