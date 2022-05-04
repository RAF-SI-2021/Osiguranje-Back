package app.services;

import app.model.Future;
import app.model.dto.FutureDTO;
import app.repositories.FuturesRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Setter
public class FuturesService {
    private Date lastupdated;

    private final FuturesRepository futuresRepository;
    public FuturesService(FuturesRepository futuresContractRepository) {
        this.futuresRepository = futuresContractRepository;
    }

    public FutureDTO findById(long id) {
        Optional<Future> opFuture = futuresRepository.findById(id);
        if(!opFuture.isPresent())
            return null;
        Future future = opFuture.get();
        FutureDTO dto = new FutureDTO(future);
        return dto;
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

    public Future save(Future future) {
        return this.futuresRepository.save(future);
    }

}
