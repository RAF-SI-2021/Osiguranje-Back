package bootstrap;

import model.Exchange;
import model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import repositories.ExchangeRepository;
import repositories.StocksRepository;
import yahoofinance.YahooFinance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class StocksBootstrap {

    @Value("${ny.stocks.symbols}")
    private String nyStocksPath;

    @Value("${na.stocks.symbols}")
    private String naStocksPath;

    private final StocksRepository stocksRepository;
    private final ExchangeRepository exchangeRepository;

    @Autowired
    public StocksBootstrap(StocksRepository stocksRepository, ExchangeRepository exchangeRepository) {
        this.stocksRepository = stocksRepository;
        this.exchangeRepository = exchangeRepository;
    }

    public void loadStocksData()  {
        try {
            String[] stocksArrNy = readStockSymbols(nyStocksPath);
            String[] stocksArrNa = readStockSymbols(naStocksPath);

            Exchange xnys = null, xnas = null;
            try{
                xnys = exchangeRepository.findByMIC("XNYS");
                xnas = exchangeRepository.findByMIC("XNAS");
            } catch (Exception e) {
                e.printStackTrace();
            }

            fetchStocks(stocksArrNy, xnys);
            fetchStocks(stocksArrNa, xnas);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Map <String, yahoofinance.Stock> resNy = YahooFinance.get(stocksArrNy, Interval.DAILY);
//        Map <String, yahoofinance.Stock> resNa = YahooFinance.get(stocksArrNa, Interval.DAILY);
    }

    private String[] readStockSymbols(String filename) throws IOException {
        ArrayList<String> stocks = new ArrayList <>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {


            String stockCode;
            while (true) {
                try {
                    if ((stockCode = br.readLine()) == null) break;
                    stocks.add(stockCode);
                } catch (IOException e) {
                }
            }
        }
        String[] stocksArr = new String[stocks.size()];
        return stocks.toArray(stocksArr);
    }

    private void fetchStocks(String[] stocksArr, Exchange stockExchange) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        for (String symbol : stocksArr) {
            List<Stock> stockExists = stocksRepository.findStockBySymbol(symbol);
            if (!stockExists.isEmpty()) {
                continue;
            }
            try {
                yahoofinance.Stock stock = YahooFinance.get(symbol);
                if (stock == null || stock.getHistory() == null || !stock.isValid()) {
                    continue;
                }

                String lastUpdated = formatter.format(date);
                String description = stock.getName();
                BigDecimal price = stock.getQuote().getPrice();
                BigDecimal ask = stock.getQuote().getAsk();
                BigDecimal bid = stock.getQuote().getBid();
                BigDecimal priceChange = stock.getQuote().getChange();
                Long volume = stock.getQuote().getVolume();
                Long outstandingShares = stock.getStats().getSharesOutstanding();

                Stock newStock = new Stock(symbol, description, stockExchange, lastUpdated, price, ask, bid, priceChange, volume, outstandingShares, null);

//                Collection <SecurityHistory> history = new ArrayList <>();
//                for (HistoricalQuote hq : stock.getHistory()) {
//                    SecurityHistory stockHistory = new SecurityHistory(hq.getOpen().toPlainString(), hq.getClose().toPlainString(),
//                            hq.getHigh().toPlainString(), hq.getLow().toPlainString());
//
//                    history.add(stockHistory);
//
//                    /* Predugo bi trajalo, dovoljno je za demonstraciju. */
//                    if (history.size() > 3) break;
//                }

//                securityHistoryRepository.saveAll(history);

                newStock.setSecurityHistory(null);
                stocksRepository.save(newStock);
            } catch (Exception e) {
            }

        }
    }
}
