package app.model.dto;

import app.model.OptionType;
import app.model.StockOption;
import lombok.Getter;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Getter
public class OptionDTO extends SecurityDTO {

    StockDTO stockListing;
    OptionType optionType;
    BigDecimal strikePrice;
    BigDecimal impliedVolatility;
    Long openInterest;
    Date settlementDate;
    BigDecimal maintenanceMargin;
    BigDecimal theta;
    int contractSize;

    public OptionDTO(StockOption option) {
        super(option);

        this.stockListing = new StockDTO(option.getStockListing());
        this.optionType = option.getOptionType();
        this.strikePrice = option.getStrikePrice();
        this.impliedVolatility = option.getImpliedVolatility();
        this.openInterest = option.getOpenInterest();
        this.settlementDate = option.getSettlementDate();
        this.contractSize = 100;

        try{
            this.maintenanceMargin = stockListing.getPrice().divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(stockListing.getContractSize()));
            this.theta = calculateTheta();
        } catch(Exception e){}
    }

    private BigDecimal calculateTheta(){
        LocalDate setDate = settlementDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        double stockPrice = this.stockListing.getPrice().doubleValue();
        double optionStrikePrice = this.strikePrice.doubleValue();
        double stockDividend = this.stockListing.getDividendYield().doubleValue();
        double optionImplVol = this.impliedVolatility.doubleValue();

        double timeToExpiration = 100.0 * ChronoUnit.DAYS.between(LocalDate.now(), setDate) / 365;

        double d1 = Math.log(stockPrice / optionStrikePrice) + (-1 * stockDividend + optionImplVol * optionImplVol / 2) * timeToExpiration;

        double theta = -1 * (stockPrice * optionImplVol * Math.exp(-1 * stockDividend * timeToExpiration + d1 * d1 / 2) / 2 * Math.sqrt(2 * timeToExpiration * Math.PI));

        NormalDistribution normalDistribution = new NormalDistribution(0, 1);

        double dividendFactor = stockDividend * stockPrice * Math.exp(-1 * stockDividend * timeToExpiration);

        if(this.optionType == OptionType.CALL) {
            theta += dividendFactor * normalDistribution.cumulativeProbability(d1);
        } else {
            theta -= dividendFactor * (1 - normalDistribution.cumulativeProbability(d1));
        }

        return BigDecimal.valueOf(theta);
    }
}
