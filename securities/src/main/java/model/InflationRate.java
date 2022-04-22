package model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Data
@Getter
public class InflationRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Currency currency;

    @Column
    private Integer year;

    @Column
    private Float value;
}
