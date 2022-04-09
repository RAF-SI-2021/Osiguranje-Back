package com.osiguranje.stocks.task;

import com.osiguranje.stocks.model.StockModel;
import com.osiguranje.stocks.repositories.StockRepository;
import com.osiguranje.stocks.services.StockService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class FetchTask {
    @Value("${ny.stocks.symbols}")
    private String nyStocksPath;

    @Value("${na.stocks.symbols}")
    private String naStocksPath;

    private StockService stockService;

    @Autowired
    public FetchTask(StockService stockService) {
        this.stockService = stockService;
    }


    private String[] getData(String filename) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        ArrayList<String> stocks = new ArrayList<>();
        String stockCode;
        while (true) {
            try {
                if ((stockCode = br.readLine()) == null) break;
                stocks.add(stockCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] stocksArr = new String[stocks.size()];
        stocksArr = stocks.toArray(stocksArr);

        return stocksArr;
    }

    private List<StockModel> getStocksToSave(String[] stocksArr, Map<String, Stock> response) throws IOException {
        List<StockModel> stocksToSave = new ArrayList<>();
        for(String s : stocksArr){
            List<StockModel> stockExists =  stockService.findBySymbol(s);
            if(!stockExists.isEmpty()){
                continue;
            }
            Stock stock = response.get(s);
            if(stock == null || stock.getHistory() == null)
                continue;
            List<HistoricalQuote> history = stock.getHistory();
            if(stock.getStats() == null || stock.getStats().getSharesOutstanding() == null)
                continue;
            Long outstanding_shares = stock.getStats().getSharesOutstanding();
            for(HistoricalQuote h : history)
                stocksToSave.add(new StockModel(
                        s,
                        outstanding_shares,
                        new Date(h.getDate().getTimeInMillis()),
                        h.getOpen().doubleValue(),
                        h.getClose().doubleValue(),
                        h.getLow().doubleValue(),
                        h.getHigh().doubleValue(),
                        h.getVolume()
                ));
        }
        return stocksToSave;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fetch(){
        try {
            String[] stocksArrNy = getData(nyStocksPath);
            String[] stocksArrNa = getData(naStocksPath);

            Map<String, Stock> resNy = YahooFinance.get(stocksArrNy, Interval.DAILY);
            Map<String, Stock> resNa = YahooFinance.get(stocksArrNa, Interval.DAILY);

            List<StockModel> stocksToSaveNy = getStocksToSave(stocksArrNy, resNy);
            List<StockModel> stocksToSaveNa = getStocksToSave(stocksArrNa, resNa);

            stockService.saveAll(stocksToSaveNy);
            stockService.saveAll(stocksToSaveNa);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
