package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class SecurityHistory {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;
    @Column
    private String date;
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal ask;
    @Column
    private BigDecimal bid;
    @Column
    private BigDecimal priceChange;
    @Column
    private Long volume;

}
