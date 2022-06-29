package otc.model;

import lombok.*;
import otc.model.dto.Company;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contract")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {


    @NotNull
    private Long contractID;
    private Company employer;
    @NotNull
    private String status;
    @NotNull
    private String creationDate;
    private String lastEdit;
    @NotNull
    private String description;
}
