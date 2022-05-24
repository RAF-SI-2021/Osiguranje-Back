package app.bootstrap;

import app.Config;
import app.model.Exchange;
import app.model.SecurityHistory;
import app.model.Stock;
import app.repositories.SecurityHistoryRepository;
import app.repositories.StocksRepository;
import app.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.repositories.ExchangeRepository;
import yahoofinance.YahooFinance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class StocksBootstrap {

//    @Value("${ny.stocks.symbols}")
//    private String nyStocksPath;
//
//    @Value("${na.stocks.symbols}")
//    private String naStocksPath;

    private final StocksRepository stocksRepository;
    private final ExchangeRepository exchangeRepository;
    private final SecurityHistoryRepository securityHistoryRepository;
    private final StockService stockService;

    @Autowired
    public StocksBootstrap(StocksRepository stocksRepository, ExchangeRepository exchangeRepository, SecurityHistoryRepository securityHistoryRepository, StockService stockService) {
        this.stocksRepository = stocksRepository;
        this.exchangeRepository = exchangeRepository;
        this.securityHistoryRepository = securityHistoryRepository;
        this.stockService = stockService;
    }

    public void loadStocksData()  {
        try {

            ClassLoader classLoader = Config.class.getClassLoader();
            File xnasFile = new File(classLoader.getResource(Config.getProperty("xnas_file")).getFile());
            String[] stocksArrNa = readStockSymbols(xnasFile);

            fetchStocks(stocksArrNa);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Map <String, yahoofinance.Stock> resNy = YahooFinance.get(stocksArrNy, Interval.DAILY);
//        Map <String, yahoofinance.Stock> resNa = YahooFinance.get(stocksArrNa, Interval.DAILY);
    }

    private String[] readStockSymbols(File file) throws IOException {
        ArrayList<String> stocks = new ArrayList <>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

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

    private void fetchStocks(String[] stocksArr) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        stockService.setLastupdated(date);

        for (String symbol : stocksArr) {
            List<Stock> stockExists = stocksRepository.findStockByTicker(symbol);
            if (!stockExists.isEmpty()) {
                continue;
            }
            try {
//                RestTemplate rest = new RestTemplate();
//                HttpHeaders headers = new HttpHeaders();
//                HttpEntity<AlphaVantageQuoteAPIResponse> entity = new HttpEntity <>(headers);
//                ResponseEntity<AlphaVantageQuoteAPIResponse> response = rest.exchange(Config.getProperty("alphavantage_quote_url") + symbol + "&apikey=" + Config.getProperty("alphavantage_api_key"), HttpMethod.GET, entity, AlphaVantageQuoteAPIResponse.class);
//                System.out.println(Config.getProperty("alphavantage_quote_url") + symbol + "&apikey=" + Config.getProperty("alphavantage_api_key"));
//                AlphaVantageQuoteAPIResponse data = Objects.requireNonNull(response.getBody());
//                System.out.println(data);

                yahoofinance.Stock stock = YahooFinance.get(symbol);
                if (stock == null || !stock.isValid()) {
                    continue;
                }

                String lastUpdated = formatter.format(date);
                String name = stock.getName();
                BigDecimal price = stock.getQuote().getPrice();
                BigDecimal ask = stock.getQuote().getAsk();
                BigDecimal bid = stock.getQuote().getBid();
                BigDecimal priceChange = stock.getQuote().getChange();
                Long volume = stock.getQuote().getVolume();
                Long outstandingShares = stock.getStats().getSharesOutstanding();
                BigDecimal dividendYield = stock.getDividend().getAnnualYield();

                Exchange stockExchange = exchangeRepository.findByAcronym(stock.getStockExchange());
                if(stockExchange == null)
                    stockExchange = exchangeRepository.findByAcronym("NASDAQ");

                Stock newStock = new Stock(symbol, name, stockExchange, lastUpdated, price, ask, bid, priceChange, volume, outstandingShares, dividendYield);

                Collection <SecurityHistory> history = new ArrayList<>();
//                for (HistoricalQuote hq : stock.getHistory()) {
//                    SecurityHistory stockHistory = new SecurityHistory(hq.getDate().toString(), hq.getClose(),
//                            hq.getHigh(), hq.getLow(), hq.getClose().subtract(hq.getOpen()), hq.getVolume());
//
//                    history.add(stockHistory);
//
//                    /* dovoljno za demonstraciju */
//                    if (history.size() > 3) break;
//                }

                newStock.setSecurityHistory(history);
                stocksRepository.save(newStock);
            } catch (Exception e) {}
        }
    }
}
