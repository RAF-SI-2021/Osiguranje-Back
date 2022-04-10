package services;

import model.Future;
import repositories.FuturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuturesService {
    private final FuturesRepository futuresRepository;

    @Autowired
    public FuturesService(FuturesRepository futuresContractRepository) {
        this.futuresRepository = futuresContractRepository;
    }

    public List<Future> getFuturesData() {
        return futuresRepository.findAll();
    }

    public Future save(Future future) {
        return this.futuresRepository.save(future);
    }
}
