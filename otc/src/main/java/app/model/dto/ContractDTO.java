package app.model.dto;


import java.util.List;

public class ContractDTO {

    private Long contractID;
    private CompanyDTO company;
    private String status;
    private String creationDate;
    private String lastEdit;
    private String description;
    private List<TransactionDTO> transactions;

}
