package app;

import models.Akcija;
import models.History;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repositories.AkcijaHistoryRepository;
import repositories.AkcijaRepository;
import repositories.BerzaRepository;
import repositories.ValutaRepository;

import models.Berza;
import models.Valuta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yahoofinance.Stock;
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

    private final String VALUTE_CSV     = "./data-loader/src/main/resources/data/valute.csv";
    private final String BERZE_CSV      = "./data-loader/src/main/resources/data/berze.csv";
    private final String AKCIJE_XNYS    = "./data-loader/src/main/resources/data/xnys_codes.csv";
    private final String AKCIJE_XNAS    = "./data-loader/src/main/resources/data/xnas_codes.csv";

    private BerzaRepository berzaRepository;
    private ValutaRepository valutaRepository;
    private AkcijaRepository akcijaRepository;
    private AkcijaHistoryRepository akcijaHistoryRepository;

    @Autowired
    public DataLoader(BerzaRepository berzaRepository, ValutaRepository valutaRepository, AkcijaRepository akcijaRepository, AkcijaHistoryRepository akcijaHistoryRepository) {
        this.berzaRepository = berzaRepository;
        this.valutaRepository = valutaRepository;
        this.akcijaRepository = akcijaRepository;
        this.akcijaHistoryRepository = akcijaHistoryRepository;
    }

    public static void main( String[] args ) {
        SpringApplication.run( DataLoader.class, args );
    }

    @Override
    public void run( String... args ) throws Exception {
        System.out.println( "############## DATA LOADER ##############" );
        Scanner input = new Scanner( System.in );

        System.out.print( "Would you like to start data-loader? [y/N]: " );
        String userInput = input.nextLine();

        if( userInput.equals( "y" ) ) {
            importCurrencies();
            importMarkets();
            importStocks( AKCIJE_XNYS, "XNYS" );
            importStocks( AKCIJE_XNAS, "XNAS" );
        } else {
            System.out.println( "Closing..." );
        }

    }

    private void importMarkets()
    {
        try {
            BufferedReader br = new BufferedReader( new FileReader( BERZE_CSV ) );

            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] columns = line.split( "," );
                Berza berza = new Berza( columns[2], columns[4], columns[1], columns[5], columns[3], columns[0] );

                Optional<Valuta> euro 			= this.valutaRepository.findByIsoCode( "EUR" );
                Optional<Valuta> dollar 		= this.valutaRepository.findByIsoCode( "USD" );
                Optional<Valuta> britishPound 	= this.valutaRepository.findByIsoCode( "GBP" );
                Collection<Valuta> valute		= new ArrayList<>( Arrays.asList( euro.get(), dollar.get(), britishPound.get() ) );

                Optional<Valuta> valutaDrzave   = valutaRepository.findByDrzava( columns[1] );
                if( valutaDrzave.isPresent() && !valute.contains( valutaDrzave.get() ) ) valute.add( valutaDrzave.get() );

                berza.setValute( valute );
                berzaRepository.save( berza );
            }

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void importCurrencies()
    {
        try {
            BufferedReader br = new BufferedReader( new FileReader( VALUTE_CSV ) );

            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] columns = line.split( "," );
                Valuta valuta = new Valuta( columns[2], columns[1], columns[3], columns[0] );
                valutaRepository.save( valuta );
            }

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void importStocks( String fileName, String oznakaBerze )
    {
        try {
            BufferedReader br = new BufferedReader( new FileReader( fileName ) );

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            Optional<Berza> xnys = this.berzaRepository.findByOznaka( oznakaBerze );

            String stockCode;
            while( ( stockCode = br.readLine() ) != null ) {
                try {
                    Stock stock = YahooFinance.get( stockCode );
                    if( stock != null ) {

                        String poslednjeAzuriranje = formatter.format( date );
                        String oznaka   = stockCode;
                        String opis     = stock.getName();
                        String cena     = stock.getQuote().getPrice().toPlainString();
                        String ask      = stock.getQuote().getAsk().toPlainString();
                        String bid      = stock.getQuote().getBid().toPlainString();
                        String promena  = stock.getQuote().getChange().toPlainString();
                        String volume   = stock.getQuote().getVolume().toString();

                        if( xnys.isPresent() ) {
                            Akcija akcija = new Akcija( oznaka, xnys.get(), opis, poslednjeAzuriranje, cena, ask, bid, promena, volume );

                            Collection<History> history = new ArrayList<>();
                            for( HistoricalQuote hq: stock.getHistory() ) {
                                History akcijaHistory = new History( hq.getOpen().toPlainString(), hq.getClose().toPlainString(),
                                        hq.getHigh().toPlainString(), hq.getLow().toPlainString() );

                                history.add( akcijaHistory );

                                /* Predugo bi trajalo, dovoljno je za demonstraciju. */
                                if( history.size() > 3 ) break;
                            }

                            akcijaHistoryRepository.saveAll( history );

                            akcija.setHistory( history );
                            akcijaRepository.save( akcija );
                        }
                    }
                } catch ( IOException e ) {
                    System.out.println( "YahooFinance API Error: can't fetch data for stock [" + stockCode + "]." );
                } catch ( NullPointerException e ) {
                    System.out.println( "YahooFinance API Error: No sufficient data for stock [" + stockCode + "]." );
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private String removePrefix( String s, String prefix ) {
        if( s.startsWith( prefix ) )
            return s.substring( prefix.length() );

        return s;
    }
}
