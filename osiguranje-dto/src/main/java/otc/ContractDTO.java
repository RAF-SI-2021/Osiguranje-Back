package otc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {

    private Long id;
    private CompanyDTO company;
    private Status status;
    private String creationDate;
    private String lastUpdated;
    private String refNumber;
    private String description;
    private List<TransactionItemDTO> transactions;
}
