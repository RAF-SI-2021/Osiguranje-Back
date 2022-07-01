package buyingmarket.mappers;

import buyingmarket.model.Actuary;
import buyingmarket.model.ActuaryType;
import buyingmarket.model.Agent;
import buyingmarket.model.Supervisor;
import buyingmarket.model.dto.ActuaryCreateDto;
import org.springframework.stereotype.Component;

@Component
public class ActuaryMapper {
    public Actuary actuaryCreateDtoToActuary(ActuaryCreateDto dto) {
        Actuary actuary;
        if (dto.getActuaryType().equals(ActuaryType.SUPERVISOR)) {
            actuary = new Supervisor(dto.getUserId());
        } else {
            actuary = new Agent(dto.getUserId(), dto.getLimit(), dto.getApprovedOrders());
        }
        return actuary;
    }
}
