package app.model.dto;

import app.model.Contract;
import app.model.Status;
import app.model.Transaction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ContractDTO {

    private Long id;
    private CompanyDTO company;
    private Status status;
    private String creationDate;
    private String lastUpdated;
    private String refNumber;
    private String description;
    private List<TransactionDTO> transactions;

    public ContractDTO(Contract contract) {
        this.id = contract.getId();
        this.company = new CompanyDTO(contract.getCompany());
        this.status = contract.getStatus();
        this.creationDate = contract.getCreationDate();
        this.lastUpdated = contract.getLastUpdated();
        this.refNumber = contract.getRefNumber();
        this.description = contract.getDescription();
        this.transactions = new ArrayList<>();

        for(Transaction transaction : contract.getTransactions()){
            this.transactions.add(new TransactionDTO(transaction));
        }
    }
}
