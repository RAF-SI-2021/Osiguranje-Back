package app.model;

import app.model.dto.ContractDTO;
import app.model.dto.CreateContractDTO;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Long id;
    @ManyToOne
    private Company company;
    @Column
    private Status status;
    @Column
    private String creationDate;
    @Column
    private String lastUpdated;
    @Column
    private String refNumber;
    @Column
    private String description;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Contract(CreateContractDTO createContractDTO) {
        this.status = Status.DRAFT;
        this.refNumber = createContractDTO.getRefNumber();
        this.description = createContractDTO.getDescription();

        setLastUpdated();
        this.creationDate = this.lastUpdated;
    }

    public void setLastUpdated(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.lastUpdated = formatter.format(date);
    }
}
