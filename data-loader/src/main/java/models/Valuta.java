package models;

import lombok.Data;
import javax.persistence.*;
import java.util.Collection;

@Entity
// @Table( name = "valute" )
@Data
public class Valuta {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;

    @Column( name = "naziv")
    private String naziv;

    @Column( name = "iso_code" )
    private String isoCode;

    @Column( name = "oznaka" )
    private String oznaka;

    @Column( name = "drzava" )
    private String drzava;

    @ManyToMany( mappedBy = "valute" )
    Collection<Berza> berze;

    // TODO: Dodati polje za procenat inflacije.


    public Valuta() { }

    public Valuta(String naziv, String isoCode, String oznaka, String drzava ) {
        this.naziv = naziv;
        this.isoCode = isoCode;
        this.oznaka = oznaka;
        this.drzava = drzava;
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Valuta valuta = ( Valuta ) o;
        return id == valuta.id;
    }

}
