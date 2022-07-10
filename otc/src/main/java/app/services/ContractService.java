package app.services;

import app.model.Contract;
import app.model.Status;
import app.model.TransactionItem;
import app.model.dto.TransactionItemDTO;
import app.repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    private final TransactionItemService transactionItemService;

    @Value("${api.transaction}")
    private String transactionsApiUrl;

    @Autowired
    public ContractService(ContractRepository contractRepository, TransactionItemService transactionItemService) {
        this.contractRepository = contractRepository;
        this.transactionItemService = transactionItemService;
    }

    public Contract save(Contract contract){
        return contractRepository.save(contract);
    }

    public List <Contract> findAll() {
        return contractRepository.findAll();
    }

    public Optional <Contract> findByID(Long id) {
        return contractRepository.findById(id);
    }

    public void createTransactionItem(Contract contract, TransactionItem transactionItem) {
        contract.getTransactionItems().add(transactionItem);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }

    public void deleteTransactionItem(Contract contract, Long transactionID) {
        transactionItemService.deleteByID(transactionID);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }


    public void updateTransactionItem(Contract contract, TransactionItemDTO transactionItemDTO) {
        transactionItemService.update(transactionItemDTO);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }

    public void finalizeContract(Contract contract) {
        contract.setStatus(Status.FINALIZED);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }

    public String getTransactionsApiServiceURL() {
        return transactionsApiUrl;
    }
}
