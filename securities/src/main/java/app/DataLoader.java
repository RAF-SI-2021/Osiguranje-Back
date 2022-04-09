package app;

import model.SecurityHistory;
import model.forex.ContractSize;
import model.forex.Currency;
import model.Forex;
import model.Future;
import model.Stock;
import model.forex.ExchangeRateAPIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import repositories.CurrencyRepository;
import repositories.SecurityHistoryRepository;
import services.ForexService;
import services.FuturesService;
import services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class DataLoader implements CommandLineRunner {
    private static final String COMMA_DELIMETER = ",";
    private static final int EUREX_SYMBOL = 0;
    private static final int EUREX_NAME = 2;
    private static final int EUREX_SETTLEMENT_MONTHS = 3;
    private static final int EUREX_CATEGORY = 5;
    private static final int CATEGORY_NAME = 0;
    private static final int CONTRACT_SIZE = 1;
    private static final int CONTRACT_UNIT = 2;
    private static final int MAINTENANCE_MARGIN = 3;

    private final FuturesService futuresService;
    private final ForexService forexService;
    private final StockService stockService;
    private final CurrencyRepository currencyRepository;
    private final SecurityHistoryRepository securityHistoryRepository;

    @Value("${ny.stocks.symbols}")
    private String nyStocksPath;

    @Value("${na.stocks.symbols}")
    private String naStocksPath;

    @Value("${currencies}")
    private String currenciesPath;

    @Value("${exchangerate.api.url}")
    private String exchangeRateUrl;

    @Autowired
    public DataLoader(FuturesService futuresService, ForexService forexService, StockService stockService, CurrencyRepository currencyRepository, SecurityHistoryRepository securityHistoryRepository) {
        this.futuresService = futuresService;
        this.forexService = forexService;
        this.stockService = stockService;
        this.currencyRepository = currencyRepository;
        this.securityHistoryRepository = securityHistoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadStocksData();
        loadForexData();
        loadFuturesData();
    }

    private void loadForexData() throws IOException {
        readCurrencies();
        List<Currency> currencies = currencyRepository.findAll();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        for(Currency currency : currencies){

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ExchangeRateAPIResponse> entity = new HttpEntity<>(headers);
            ResponseEntity<ExchangeRateAPIResponse> response = null;
            response = rest.exchange(exchangeRateUrl + currency.getIsoCode(), HttpMethod.GET, entity, ExchangeRateAPIResponse.class);
            HashMap<String, BigDecimal> rates = response.getBody().getConversionRates();

            for(Currency c2 : currencies){
                if(c2.equals(currency))
                    continue;
                String symbol = currency.getIsoCode() + c2.getIsoCode();
                List<Forex> forexExists = forexService.findBySymbol(symbol);
                if (!forexExists.isEmpty()) {
                    continue;
                }
                yahoofinance.Stock stock = YahooFinance.get(symbol);
                if (stock == null || stock.getHistory() == null)
                    continue;

                String lastUpdated = formatter.format(date);
                String description = stock.getName();
                BigDecimal price = stock.getQuote().getPrice();
                //BigDecimal price = rates.get(c2.getIsoCode()); // ako ne radi yf
                BigDecimal ask = stock.getQuote().getAsk();
                BigDecimal bid = stock.getQuote().getBid();
                BigDecimal priceChange = stock.getQuote().getChange();
                Long volume = stock.getQuote().getVolume();

                Forex newForex = new Forex(symbol, description, lastUpdated, price, ask, bid, priceChange, volume);
                newForex.setBaseCurrency(currency.getIsoCode());
                newForex.setQuoteCurrency(c2.getIsoCode());
                newForex.setContractSize(ContractSize.STANDARD.getSize());

                Collection<SecurityHistory> history = new ArrayList<>();
                for (HistoricalQuote hq : stock.getHistory()) {
                    SecurityHistory stockHistory = new SecurityHistory(hq.getOpen().toPlainString(), hq.getClose().toPlainString(),
                            hq.getHigh().toPlainString(), hq.getLow().toPlainString());

                    history.add(stockHistory);

                    /* Predugo bi trajalo, dovoljno je za demonstraciju. */
                    if (history.size() > 3) break;
                }

                securityHistoryRepository.saveAll(history);

                newForex.setSecurityHistory(history);
                forexService.save(newForex);
            }
        }
    }

    private void readCurrencies()
    {
        try {
            BufferedReader br = new BufferedReader( new FileReader( currenciesPath ) );

            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] columns = line.split( "," );
                Currency currency = new Currency( columns[2], columns[1], columns[3], columns[0] );
                currencyRepository.save(currency);
            }

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void loadStocksData() throws IOException {
        String[] stocksArrNy = readStockSymbols(nyStocksPath);
        String[] stocksArrNa = readStockSymbols(naStocksPath);

        Map<String, yahoofinance.Stock> resNy = YahooFinance.get(stocksArrNy, Interval.DAILY);
        Map<String, yahoofinance.Stock> resNa = YahooFinance.get(stocksArrNa, Interval.DAILY);

        fetchStocks(stocksArrNy, resNy);
        fetchStocks(stocksArrNa, resNa);
    }

    private void fetchStocks(String[] stocksArr, Map<String, yahoofinance.Stock> response) throws IOException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        for(String symbol : stocksArr) {
            List<Stock> stockExists = stockService.findBySymbol(symbol);
            if (!stockExists.isEmpty()) {
                continue;
            }
            yahoofinance.Stock stock = YahooFinance.get(symbol);
            if (stock == null || stock.getHistory() == null)
                continue;

            String lastUpdated = formatter.format(date);
            String description = stock.getName();
            BigDecimal price = stock.getQuote().getPrice();
            BigDecimal ask = stock.getQuote().getAsk();
            BigDecimal bid = stock.getQuote().getBid();
            BigDecimal priceChange = stock.getQuote().getChange();
            Long volume = stock.getQuote().getVolume();
            Long outstandingShares = stock.getStats().getSharesOutstanding();

            Stock newStock = new Stock(symbol, description, lastUpdated, price, ask, bid, priceChange, volume, outstandingShares);

            Collection<SecurityHistory> history = new ArrayList<>();
            for (HistoricalQuote hq : stock.getHistory()) {
                SecurityHistory stockHistory = new SecurityHistory(hq.getOpen().toPlainString(), hq.getClose().toPlainString(),
                        hq.getHigh().toPlainString(), hq.getLow().toPlainString());

                history.add(stockHistory);

                /* Predugo bi trajalo, dovoljno je za demonstraciju. */
                if (history.size() > 3) break;
            }

            securityHistoryRepository.saveAll(history);

            newStock.setSecurityHistory(history);
            stockService.save(newStock);
        }
    }

    private String[] readStockSymbols(String filename) throws FileNotFoundException {
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

    private void loadFuturesData() throws Exception{
        List<List<String>> eurexData = new ArrayList<>();
        List<List<String>> categoryData = new ArrayList<>();
        File file = new File(Config.getProperty("eurex_file"));
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file.getCanonicalPath()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMETER);
                List<String> list = Arrays.asList(values);
                if (list.size() > 5) {
                    eurexData.add(list);
                }
            }
            file = new File(Config.getProperty("categories_file"));
            br = new BufferedReader(new FileReader(file.getCanonicalPath()));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMETER);
                List<String> list = Arrays.asList(values);
                categoryData.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert br != null;
            br.close();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        for (List<String> eurexList : eurexData) {
            for (List<String> categoryList : categoryData) {
                if (eurexList.get(EUREX_CATEGORY).equals(categoryList.get(CATEGORY_NAME))) {
                    String symbol = eurexList.get(EUREX_SYMBOL);
                    String description = eurexList.get(EUREX_NAME);
                    Integer contractSize = Integer.parseInt(categoryList.get(CONTRACT_SIZE));
                    String contractUnit = categoryList.get(CONTRACT_UNIT);
                    Integer maintenanceMargin = Integer.parseInt(categoryList.get(MAINTENANCE_MARGIN));
                    String settlementDates = eurexList.get(EUREX_SETTLEMENT_MONTHS);
                    System.out.println(settlementDates);
                    char[] months = new char[settlementDates.length()];
                    for (int i = 0; i < settlementDates.length(); i++) {
                        months[i] = settlementDates.toUpperCase().charAt(i);
                    }
                    int year = 2022;
                    Date settlementDate;
                    for (char c : months) {
                        settlementDate = getDateForMonth(c, year);

                        yahoofinance.Stock stock = YahooFinance.get(symbol + c + "22");
                        if (stock == null || stock.getHistory() == null){
                            System.out.println("YF returned null for future symbol " + symbol);
                            continue;
                        }
                        String lastUpdated = formatter.format(date);
                        BigDecimal price = stock.getQuote().getPrice();
                        BigDecimal ask = stock.getQuote().getAsk();
                        BigDecimal bid = stock.getQuote().getBid();
                        BigDecimal priceChange = stock.getQuote().getChange();
                        Long volume = stock.getQuote().getVolume();

                        Collection<SecurityHistory> history = new ArrayList<>();
                        for (HistoricalQuote hq : stock.getHistory()) {
                            SecurityHistory stockHistory = new SecurityHistory(hq.getOpen().toPlainString(), hq.getClose().toPlainString(),
                                    hq.getHigh().toPlainString(), hq.getLow().toPlainString());
                            history.add(stockHistory);
                            if (history.size() > 3) break;
                        }
                        securityHistoryRepository.saveAll(history);

                        Future newFuture = new Future(symbol, description, lastUpdated, price, ask, bid, priceChange, volume, contractSize, contractUnit, maintenanceMargin, settlementDate);
                        newFuture.setSecurityHistory(history);
                        futuresService.save(newFuture);

                        System.out.println(newFuture);
                        year++;             // todo ??
                    }
                }
            }
        }
    }

    private Date getDateForMonth(char month, int year) throws ParseException {
        Date settlementDate;
        switch (month) {
            case 'F':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("01", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("01", year));
                }
                break;
            case 'G':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("02", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("02", year));
                }
                break;
            case 'H':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("03", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("03", year));
                }
                break;
            case 'J':
                if (year == 2022) {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("04", year + 1));
                } else {
                    settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("04", year));
                }
                break;
            case 'K':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("05", year));
                break;
            case 'M':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("06", year));
                break;
            case 'N':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("07", year));
                break;
            case 'Q':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("08", year));
                break;
            case 'U':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("09", year));
                break;
            case 'V':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("10", year));
                break;
            case 'X':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("11", year));
                break;
            case 'Z':
                settlementDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateFormatBuilder("12", year));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        return settlementDate;
    }

    private String dateFormatBuilder(String month, int year) {
        return "01/" + month + "/" + year;
    }
}
