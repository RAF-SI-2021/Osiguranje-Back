package com.osiguranjeback.forex.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Forex {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "base_currency", nullable = false)
    private String baseCurrency;
    @Column(name = "quote_currency", nullable = false)
    private String quoteCurrency;
    @Column(name = "contract_size", nullable = false)
    private Integer contractSize;
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public BigDecimal nominalValue(BigDecimal lotSize){
        return lotSize.multiply(price);
    }
}
