package app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    String name;
    @Column
    String code;
    @OneToOne
    Currency currency;

    public Region(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(name, region.name) &&
                Objects.equals(code, region.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
