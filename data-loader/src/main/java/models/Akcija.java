package models;


import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
public class Akcija {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;

    @Column
    private String oznaka;

    @OneToOne
    private Berza berza;

    @Column
    private String opis;

    @Column
    private String poslednjeAzuriranje;

    @Column
    private String cena;

    @Column
    private String ask;

    @Column
    private String bid;

    @Column
    private String promena;

    @Column
    private String volume;

    @OneToMany
    private Collection<History> history;

    public Akcija() {  }

    public Akcija( String oznaka, Berza berza, String opis, String poslednjeAzuriranje, String cena, String ask, String bid, String promena, String volume ) {
        this.oznaka = oznaka;
        this.berza = berza;
        this.opis = opis;
        this.poslednjeAzuriranje = poslednjeAzuriranje;
        this.cena = cena;
        this.ask = ask;
        this.bid = bid;
        this.promena = promena;
        this.volume = volume;
    }
}
