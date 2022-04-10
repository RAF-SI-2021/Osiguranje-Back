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
import repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private final FuturesRepository futuresRepository;
    private final ForexRepository forexRepository;
    private final StocksRepository stocksRepository;
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
    public DataLoader(FuturesRepository futuresRepository, ForexRepository forexRepository, StocksRepository stocksRepository, CurrencyRepository currencyRepository, SecurityHistoryRepository securityHistoryRepository) {
        this.futuresRepository = futuresRepository;
        this.forexRepository = forexRepository;
        this.stocksRepository = stocksRepository;
        this.currencyRepository = currencyRepository;
        this.securityHistoryRepository = securityHistoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadStocksData();
        loadForexData();
        loadFuturesData();
    }

    private void loadForexData() {
        readCurrencies();
        List <Currency> currencies = currencyRepository.findAll();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        for (Currency currency : currencies) {

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <ExchangeRateAPIResponse> entity = new HttpEntity <>(headers);
            ResponseEntity <ExchangeRateAPIResponse> response = rest.exchange(exchangeRateUrl + currency.getIsoCode(), HttpMethod.GET, entity, ExchangeRateAPIResponse.class);
            HashMap <String, BigDecimal> rates;
            try {
                rates = Objects.requireNonNull(response.getBody()).getConversionRates();
            } catch (Exception e) {
                continue;
            }
            for (Currency c2 : currencies) {
                if (c2.equals(currency))
                    continue;


                String symbol = currency.getIsoCode() + c2.getIsoCode();
                List <Forex> forexExists = forexRepository.findForexBySymbol(symbol);
                if (!forexExists.isEmpty()) {
                    continue;
                }
                try {
                    BigDecimal price = rates.get(c2.getIsoCode());
                    String lastUpdated = formatter.format(date);
                    BigDecimal ask = price;
                    BigDecimal bid = price;
                    BigDecimal priceChange = price;
                    Long volume = price.longValue();

                    Forex newForex = new Forex(symbol, symbol, lastUpdated, price, ask, bid, priceChange, volume);
                    newForex.setBaseCurrency(currency.getIsoCode());
                    newForex.setQuoteCurrency(c2.getIsoCode());
                    newForex.setContractSize(ContractSize.STANDARD.getSize());
                    newForex.setSecurityHistory(null);
                    forexRepository.save(newForex);
                } catch (Exception e) {
                }
            }
        }
    }

    private void readCurrencies() {
        try (BufferedReader br = new BufferedReader(new FileReader(currenciesPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                Currency currency = new Currency(columns[2], columns[1], columns[3], columns[0]);
                currencyRepository.save(currency);
            }

        } catch (IOException e) {
        }
    }

    private void loadStocksData() throws IOException {
        String[] stocksArrNy = readStockSymbols(nyStocksPath);
        String[] stocksArrNa = readStockSymbols(naStocksPath);

        Map <String, yahoofinance.Stock> resNy = YahooFinance.get(stocksArrNy, Interval.DAILY);
        Map <String, yahoofinance.Stock> resNa = YahooFinance.get(stocksArrNa, Interval.DAILY);

        fetchStocks(stocksArrNy, resNy);
        fetchStocks(stocksArrNa, resNa);
    }

    private void fetchStocks(String[] stocksArr, Map <String, yahoofinance.Stock> response) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        for (String symbol : stocksArr) {
            List <Stock> stockExists = stocksRepository.findStockBySymbol(symbol);
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

                Stock newStock = new Stock(symbol, description, lastUpdated, price, ask, bid, priceChange, volume, outstandingShares);

                Collection <SecurityHistory> history = new ArrayList <>();
                for (HistoricalQuote hq : stock.getHistory()) {
                    SecurityHistory stockHistory = new SecurityHistory(hq.getOpen().toPlainString(), hq.getClose().toPlainString(),
                            hq.getHigh().toPlainString(), hq.getLow().toPlainString());

                    history.add(stockHistory);

                    /* Predugo bi trajalo, dovoljno je za demonstraciju. */
                    if (history.size() > 3) break;
                }

                securityHistoryRepository.saveAll(history);

                newStock.setSecurityHistory(history);
                stocksRepository.save(newStock);
            } catch (Exception e) {
            }

        }
    }


    public static BigDecimal random(int range) {
        BigDecimal max = new BigDecimal(range);
        BigDecimal randFromDouble = BigDecimal.valueOf(Math.random());
        BigDecimal actualRandomDec = randFromDouble.multiply(max);
        actualRandomDec = actualRandomDec.setScale(2, RoundingMode.DOWN);
        return actualRandomDec;
    }


    private String[] readStockSymbols(String filename) throws IOException {
        ArrayList <String> stocks = new ArrayList <>();
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

    private void loadFuturesData() throws Exception {
        List <List <String>> eurexData = new ArrayList <>();
        List <List <String>> categoryData = new ArrayList <>();
        File file = new File(Config.getProperty("eurex_file"));
        try (BufferedReader br = new BufferedReader(new FileReader(file.getCanonicalPath()));) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMETER);
                List <String> list = Arrays.asList(values);
                if (list.size() > 5) {
                    eurexData.add(list);
                }
            }
            file = new File(Config.getProperty("categories_file"));
            try (BufferedReader brr = new BufferedReader(new FileReader(file.getCanonicalPath()))){
                while ((line = brr.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMETER);
                    List <String> list = Arrays.asList(values);
                    categoryData.add(list);
                }
            }

        } catch (IOException e) {
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        for (List <String> eurexList : eurexData) {
            for (List <String> categoryList : categoryData) {
                if (eurexList.get(EUREX_CATEGORY).equals(categoryList.get(CATEGORY_NAME))) {
                    String symbol = eurexList.get(EUREX_SYMBOL);
                    String description = eurexList.get(EUREX_NAME);
                    Integer contractSize = Integer.parseInt(categoryList.get(CONTRACT_SIZE));
                    String contractUnit = categoryList.get(CONTRACT_UNIT);
                    Integer maintenanceMargin = Integer.parseInt(categoryList.get(MAINTENANCE_MARGIN));
                    String settlementDates = eurexList.get(EUREX_SETTLEMENT_MONTHS);
                    char[] months = new char[settlementDates.length()];
                    for (int i = 0; i < settlementDates.length(); i++) {
                        months[i] = settlementDates.toUpperCase().charAt(i);
                    }
                    int year = 2022;
                    Date settlementDate;
                    for (char c : months) {
                        settlementDate = getDateForMonth(c, year);


                        String lastUpdated = formatter.format(date);
                        BigDecimal price = random(1000);
                        BigDecimal ask = random(1000);
                        BigDecimal bid = random(1000);
                        BigDecimal priceChange = random(1000);
                        Long volume = random(1000).longValue();
                        Future newFuture = new Future(symbol, description, lastUpdated, price, ask, bid, priceChange, volume, contractSize, contractUnit, maintenanceMargin, settlementDate);
                        newFuture.setSecurityHistory(null);
                        futuresRepository.save(newFuture);
                        year++;
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
