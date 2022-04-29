package app.mappers;

import app.model.Exchange;
import app.model.dto.ExchangeDTO;
import org.springframework.stereotype.Component;

@Component
public class ExchangeMapper {
    public Exchange exchangeDtoToExchange(ExchangeDTO dto) {
        return new Exchange(
            dto.getName(),
            dto.getAcronym(),
            dto.getMIC(),
            dto.getCountry(),
            dto.getTimeZone(),
            dto.getOpen(),
            dto.getClosed()
        );
    }
}
