package com.osiguranje.stocks.services;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.AlphaVantageException;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.fundamentaldata.response.CompanyOverviewResponse;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphavantageService {
    static {
        Config cfg = Config.builder()
                .key("V2VDC8YINO9I7FHF")
                .timeOut(10)
                .build();

        AlphaVantage.api().init(cfg);
    }
    @Autowired
    public AlphavantageService() {
    }

    public TimeSeriesResponse getTimeSeries(String stock){
        return AlphaVantage.api()//.fundamentalData().companyOverview().forSymbol(stock).fetchSync();
                .timeSeries()
                .daily()
                .forSymbol(stock)
                .outputSize(OutputSize.FULL)
                .fetchSync();
    }

    public CompanyOverviewResponse getFundamental(String company){
        return AlphaVantage.api().fundamentalData().companyOverview().forSymbol(company).fetchSync();
    }
}
