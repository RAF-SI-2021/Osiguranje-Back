package app;

import models.Stock;
import models.StockHistory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repositories.StockHistoryRepository;
import repositories.StockRepository;
import repositories.StockExchangeRepository;
import repositories.CurrencyRepository;

import models.StockExchange;
import models.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
@EnableJpaRepositories( "repositories" )
@EntityScan( "models" )
public class DataLoader implements CommandLineRunner {

    private final String VALUTE_CSV     = "src/main/resources/data/valute.csv";
    private final String BERZE_CSV      = "src/main/resources/data/berze.csv";
    private final String AKCIJE_XNYS    = "src/main/resources/data/xnys_codes.csv";
    private final String AKCIJE_XNAS    = "src/main/resources/data/xnas_codes.csv";

    private StockExchangeRepository stockExchangeRepository;
    private CurrencyRepository currencyRepository;
    private StockRepository stockRepository;
    private StockHistoryRepository stockHistoryRepository;

    @Autowired
    public DataLoader(StockExchangeRepository stockExchangeRepository, CurrencyRepository currencyRepository, StockRepository stockRepository, StockHistoryRepository stockHistoryRepository) {
        this.stockExchangeRepository = stockExchangeRepository;
        this.currencyRepository = currencyRepository;
        this.stockRepository = stockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public static void main( String[] args ) {
        SpringApplication.run( DataLoader.class, args );
    }

    @Override
    public void run( String... args ) throws Exception {
        System.out.println( "############## DATA LOADER ##############" );
            importCurrencies();
            importMarkets();
            importStocks( AKCIJE_XNYS, "XNYS" );
            importStocks( AKCIJE_XNAS, "XNAS" );
    }

    private void importMarkets()
    {
        try {
            BufferedReader br = new BufferedReader( new FileReader( BERZE_CSV ) );

            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] columns = line.split( "," );
                StockExchange stockExchange = new StockExchange( columns[2], columns[4], columns[1], columns[5], columns[3], columns[0] );

                Optional<Currency> euro = this.currencyRepository.findByIsoCode( "EUR" );
                Optional<Currency> dollar = this.currencyRepository.findByIsoCode( "USD" );
                Optional<Currency> britishPound = this.currencyRepository.findByIsoCode( "GBP" );
                Collection<Currency> currencies	= new ArrayList<>( Arrays.asList( euro.get(), dollar.get(), britishPound.get() ) );

                Optional<Currency> countryCurrency = currencyRepository.findByRegion( columns[1] );
                if( countryCurrency.isPresent() && !currencies.contains( countryCurrency.get() ) ) currencies.add( countryCurrency.get() );

                stockExchange.setCurrencies( currencies );
                stockExchangeRepository.save(stockExchange);
            }

        } catch ( IOException e ) {
            System.out.println(e);
        }
    }

    private void importCurrencies()
    {
        try {
            BufferedReader br = new BufferedReader( new FileReader( VALUTE_CSV ) );

            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] columns = line.split( "," );
                Currency currency = new Currency( columns[2], columns[1], columns[3], columns[0] );
                currencyRepository.save( currency );
            }

        } catch ( IOException e ) {
            System.out.println(e);
        }
    }

    private void importStocks( String fileName, String exchangeSymbol )
    {
        try {
            BufferedReader br = new BufferedReader( new FileReader( fileName ) );

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            Optional<StockExchange> xnys = this.stockExchangeRepository.findByMic( exchangeSymbol );

            String stockCode;
            while( ( stockCode = br.readLine() ) != null ) {
                try {
                    yahoofinance.Stock stock = YahooFinance.get( stockCode );
                    if( stock != null ) {

                        String lastUpdated = formatter.format( date );
                        String symbol = stockCode;
                        String description = stock.getName();
                        String price = stock.getQuote().getPrice().toPlainString();
                        String ask = stock.getQuote().getAsk().toPlainString();
                        String bid = stock.getQuote().getBid().toPlainString();
                        String priceChange = stock.getQuote().getChange().toPlainString();
                        String volume = stock.getQuote().getVolume().toString();

                        if( xnys.isPresent() ) {
                            Stock newStock = new Stock( symbol, xnys.get(), description, lastUpdated, price, ask, bid, priceChange, volume );

                            Collection<StockHistory> history = new ArrayList<>();
                            for( HistoricalQuote hq: stock.getHistory() ) {
                                StockHistory stockHistory = new StockHistory( hq.getOpen().toPlainString(), hq.getClose().toPlainString(),
                                        hq.getHigh().toPlainString(), hq.getLow().toPlainString() );

                                history.add( stockHistory );

                                /* Predugo bi trajalo, dovoljno je za demonstraciju. */
                                if( history.size() > 3 ) break;
                            }

                            stockHistoryRepository.saveAll( history );

                            newStock.setStockHistory( history );
                            stockRepository.save( newStock );
                        }
                    }
                } catch ( IOException e ) {
                    System.out.println( "YahooFinance API Error: can't fetch data for stock [" + stockCode + "]." );
                } catch ( NullPointerException e ) {
                    System.out.println( "YahooFinance API Error: No sufficient data for stock [" + stockCode + "]." );
                }
            }
        } catch ( IOException e ) {
            System.out.println(e);
        }
    }

    private String removePrefix( String s, String prefix ) {
        if( s.startsWith( prefix ) )
            return s.substring( prefix.length() );

        return s;
    }
}
