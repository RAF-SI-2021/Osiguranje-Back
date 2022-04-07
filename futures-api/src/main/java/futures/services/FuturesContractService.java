package futures.services;

import futures.model.FuturesContract;
import futures.repositories.FuturesContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuturesContractService {
    private final FuturesContractRepository futuresContractRepository;

    @Autowired
    public FuturesContractService(FuturesContractRepository futuresContractRepository) {
        this.futuresContractRepository = futuresContractRepository;
    }

    public List<FuturesContract> getFuturesContractData() {
        return futuresContractRepository.findAll();
    }

    public FuturesContract save(FuturesContract futuresContract) {
        return this.futuresContractRepository.save(futuresContract);
    }
}
