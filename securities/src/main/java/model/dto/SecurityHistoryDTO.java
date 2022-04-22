package model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Security;
import model.SecurityHistory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class SecurityHistoryDTO {

    private String date;
    private BigDecimal price;
    private BigDecimal ask;
    private BigDecimal bid;
    private BigDecimal change;
    private Long volume;

    public SecurityHistoryDTO(SecurityHistory securityHistory) {
        this.date = securityHistory.getDate();
        this.price = securityHistory.getPrice();
        this.ask = securityHistory.getAsk();
        this.bid = securityHistory.getBid();
        this.change = securityHistory.getChange();
        this.volume = securityHistory.getVolume();
    }
}
