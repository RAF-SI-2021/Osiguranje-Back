package app.mappers;

import app.model.Forex;
import app.model.Future;
import app.model.Security;
import app.model.Stock;
import app.model.dto.SecurityDTO;
import org.springframework.stereotype.Component;

@Component
public class SecurityMapper {
    public SecurityDTO forexToSecurityDto(Forex forex) {
        return new SecurityDTO(new Security(
                forex.getTicker(),
                forex.getName(),
                forex.getExchange(),
                forex.getLastUpdated(),
                forex.getPrice(),
                forex.getAsk(),
                forex.getBid(),
                forex.getPriceChange(),
                forex.getVolume(),
                forex.getContractSize()
        ));
    }

    public SecurityDTO futureToSecurityDto(Future future) {
        return new SecurityDTO(new Security(
            future.getTicker(),
                future.getName(),
                future.getExchange(),
                future.getLastUpdated(),
                future.getPrice(),
                future.getAsk(),
                future.getBid(),
                future.getPriceChange(),
                future.getVolume(),
                future.getContractSize()
        ));
    }

    public SecurityDTO stockToSecurityDto(Stock stock) {
        return new SecurityDTO(new Security(
                stock.getTicker(),
                stock.getName(),
                stock.getExchange(),
                stock.getLastUpdated(),
                stock.getPrice(),
                stock.getAsk(),
                stock.getBid(),
                stock.getPriceChange(),
                stock.getVolume(),
                stock.getContractSize()
        ));
    }
}
