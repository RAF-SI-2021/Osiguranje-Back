package models;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
// @Table( name = "berze" )
@Data
public class Berza {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;

    @Column( name = "naziv")
    private String naziv;

    @Column( name = "oznaka" )
    private String oznaka;

    @Column( name = "drzava" )
    private String drzava;

    @Column( name = "vremenska_zona" )
    private String timeZone;            /* UTC se podrazumeva, samo "+h" za sad. */

    @Column( name = "open" )
    private String open;

    @Column( name = "close" )
    private String closed;

    @ManyToMany
    @JoinTable(
            name = "berza_valute",
            joinColumns = @JoinColumn( name = "berza_id" ),
            inverseJoinColumns = @JoinColumn( name = "valuta_id" ) )
    Collection<Valuta> valute;

    @OneToMany
    Collection<Akcija> akcije;

    public Berza() { }

    public Berza(String naziv, String oznaka, String drzava, String timeZone, String open, String closed ) {
        this.naziv = naziv;
        this.oznaka = oznaka;
        this.drzava = drzava;
        this.timeZone = timeZone;
        this.open = open;
        this.closed = closed;
    }
}
