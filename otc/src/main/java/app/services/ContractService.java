package app.services;

import app.model.Contract;
import app.model.Status;
import app.model.Transaction;
import app.model.dto.TransactionDTO;
import app.repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final TransactionService transactionService;

    @Autowired
    public ContractService(ContractRepository contractRepository, TransactionService transactionService) {
        this.contractRepository = contractRepository;
        this.transactionService = transactionService;
    }

    public void save(Contract contract){
        contractRepository.save(contract);
    }

    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Optional<Contract> findByID(Long id){
        return contractRepository.findById(id);
    }

    public void createTransaction(Contract contract, Transaction transaction) {
        contract.getTransactions().add(transaction);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }

    public void deleteTransaction(Contract contract, Long transactionID) {
        transactionService.deleteByID(transactionID);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }


    public void updateTransaction(Contract contract, TransactionDTO transactionDTO) {
        transactionService.update(transactionDTO);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }

    public void finalize(Contract contract) {
        contract.setStatus(Status.FINALIZED);
        contract.setLastUpdated();
        contractRepository.save(contract);
    }

}
