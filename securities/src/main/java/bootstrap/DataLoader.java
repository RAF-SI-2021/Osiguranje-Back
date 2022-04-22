package bootstrap;

import app.Config;
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
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class DataLoader implements CommandLineRunner {

    private final StocksBootstrap stocksBootstrap;
    private final FuturesBootstrap futuresBootstrap;
    private final ForexBootstrap forexBootstrap;

    @Autowired
    public DataLoader(StocksBootstrap stocksBootstrap, FuturesBootstrap futuresBootstrap, ForexBootstrap forexBootstrap) {
        this.stocksBootstrap = stocksBootstrap;
        this.futuresBootstrap = futuresBootstrap;
        this.forexBootstrap = forexBootstrap;
    }

    @Override
    public void run(String... args) throws Exception {
        forexBootstrap.loadForexData();
        futuresBootstrap.loadFuturesData();
        stocksBootstrap.loadStocksData();
    }

}
