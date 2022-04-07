package models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class StockHistory {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;

    @Column
    private String open;

    @Column
    private String close;

    @Column
    private String high;

    @Column
    private String low;

//    @ManyToOne
//    private Akcija akcija;

    public StockHistory(String open, String close, String high, String low ) {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

//    public Akcija getAkcija() {
//        return akcija;
//    }
//
//    public void setAkcija(Akcija akcija) {
//        this.akcija = akcija;
//    }
}
