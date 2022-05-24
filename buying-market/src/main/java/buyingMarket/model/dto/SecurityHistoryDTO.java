package buyingMarket.model.dto;

import buyingMarket.model.SecurityHistory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
        this.change = securityHistory.getPriceChange();
        this.volume = securityHistory.getVolume();
    }
}
