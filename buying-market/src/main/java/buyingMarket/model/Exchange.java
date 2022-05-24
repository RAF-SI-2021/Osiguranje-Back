package buyingMarket.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Exchange {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private long id;
    @Column
    private String name;
    @Column
    private String acronym;     // TODO
    @Column
    private String MIC;
    @ManyToOne
    private Region region;
    @Column
    private String timeZone;    /* UTC se podrazumeva, samo "+h" za sad. */
    @Column
    private String open;
    @Column
    private String closed;
    @ManyToOne
    private Currency currency;
    @OneToMany
    Collection<Stock> stocks;

    public Exchange() { }

    public Exchange(String name, String acronym, String mic, String timeZone, String open, String closed) {
        this.name = name;
        this.acronym = acronym;
        this.MIC = mic;
        this.timeZone = timeZone;
        this.open = open;
        this.closed = closed;
    }
}
