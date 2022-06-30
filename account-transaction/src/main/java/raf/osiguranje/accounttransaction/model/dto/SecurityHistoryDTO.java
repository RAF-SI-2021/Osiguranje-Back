package raf.osiguranje.accounttransaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


public class SecurityHistoryDTO {

    private String date;
    private BigDecimal price;
    private BigDecimal ask;
    private BigDecimal bid;
    private BigDecimal change;
    private Long volume;

    public SecurityHistoryDTO(String date, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal change, Long volume) {
        this.date = date;
        this.price = price;
        this.ask = ask;
        this.bid = bid;
        this.change = change;
        this.volume = volume;
    }

    public SecurityHistoryDTO() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }
}
