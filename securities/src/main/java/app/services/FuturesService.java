package app.services;

import app.mappers.SecurityMapper;
import app.model.Future;
import app.model.dto.FutureDTO;
import app.model.dto.SecurityDTO;
import app.repositories.FuturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuturesService {
    private final FuturesRepository futuresRepository;
    private final SecurityMapper securityMapper;

    @Autowired
    public FuturesService(FuturesRepository futuresContractRepository, SecurityMapper securityMapper) {
        this.futuresRepository = futuresContractRepository;
        this.securityMapper = securityMapper;
    }

    public List<Future> getFuturesData() {
        return futuresRepository.findAll();
    }

    public List<FutureDTO> getFutureDTOData(){
        List<Future> futureList = getFuturesData();
        List<FutureDTO> dtoList = new ArrayList<>();
        for (Future f: futureList){
            dtoList.add(new FutureDTO(f));
        }
        return dtoList;
    }

    public SecurityDTO findById(long id) {
        Optional<Future> future = futuresRepository.findById(id);
        return future.map(securityMapper::futureToSecurityDto).orElse(null);
    }

    public Future save(Future future) {
        return this.futuresRepository.save(future);
    }
}
