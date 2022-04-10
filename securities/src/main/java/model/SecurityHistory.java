package model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class SecurityHistory {

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

    public SecurityHistory(String open, String close, String high, String low ) {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

}
