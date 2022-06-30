package app.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long contractID;
    @ManyToOne
    private Company company;
    @Column
    private Status status;
    @Column
    private String creationDate;
    @Column
    private String lastUpdated;
    @Column
    private String description;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    private enum Status{
        DRAFT, FINALIZED
    }

    public Contract(Long contractID, Company company, String creationDate, String lastUpdated, String description) {
        this.contractID = contractID;
        this.company = company;
        this.status = Status.DRAFT;
        this.creationDate = creationDate;
        this.lastUpdated = lastUpdated;
        this.description = description;
    }
}
