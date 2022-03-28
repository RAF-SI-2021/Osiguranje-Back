package main.java;

import main.java.models.Berza;
import main.java.models.Valuta;
import main.java.repositories.BerzaRepository;
import main.java.repositories.ValutaRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class DataLoader implements CommandLineRunner {

    private BerzaRepository berzaRepository;
    private ValutaRepository valutaRepository;

    @Autowired
    public DataLoader( BerzaRepository berzaRepository, ValutaRepository valutaRepository ) {
        this.berzaRepository = berzaRepository;
        this.valutaRepository = valutaRepository;
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
        } else {
            System.out.println( "Closing..." );
        }

    }

    private void importMarkets()
    {
        try {
            Document doc = Jsoup.connect( "https://en.wikipedia.org/wiki/List_of_stock_exchanges#Major_stock_exchanges" ).get();
            Element table = doc.getElementById( "exchanges_table" );

            for( Element row: table.getElementsByTag( "tbody" ).get( 0 ).getElementsByTag( "tr" ) ) {
                /* Trebaju nam indeksi: 2, 3, 4, 5, 9, 11, 12. */
                Elements columns = row.getElementsByTag("td");

                if ( !columns.isEmpty() ) {

                    String stockExchange, mic, region, utc, localOpen, localClosed;

                    /* Slucaj za severne zemlje jer ta tabela nema prve dve kolone. */
                    if( columns.size() < 16 ) {
                        stockExchange 	= columns.get( 0 ).getAllElements().last().text();
                        mic				= columns.get( 1 ).getAllElements().last().text();
                        region 			= columns.get( 2 ).getElementsByTag( "a" ).first().text();

                        try {
                            utc 		= columns.get( 5 ).text();
                            localOpen 	= columns.get( 7 ).text();
                            localClosed	= columns.get( 8 ).text();

                        } catch ( IndexOutOfBoundsException iobe ) {
                            /* Nema podataka. */
                            continue;
                        }
                    } else {
                        stockExchange 	= columns.get( 2 ).getAllElements().last().text();
                        mic				= columns.get( 3 ).getAllElements().last().text();

                        Element regionElement = columns.get( 4 );
                        region = ( regionElement.hasText() ) ? regionElement.text() : regionElement.getElementsByClass( "nowrap" ).text();

                        try {
                            utc 		= columns.get( 9 ).text();
                            localOpen 	= columns.get( 11 ).text();
                            localClosed	= columns.get( 12 ).text();

                        } catch ( IndexOutOfBoundsException iobe ) {
                            /* Nema podataka. */
                            continue;
                        }
                    }

                    if( localOpen.equals( "" ) || !stockExchange.contains( "Exchange" ) ) continue;

                    region = removePrefix( region, "the " );

                    Optional<Valuta> euro 			= this.valutaRepository.findByIsoCode( "EUR" );
                    Optional<Valuta> dollar 		= this.valutaRepository.findByIsoCode( "USD" );
                    Optional<Valuta> britishPound 	= this.valutaRepository.findByIsoCode( "GBP" );
                    Collection<Valuta> valute		= new ArrayList<>( Arrays.asList( euro.get(), dollar.get(), britishPound.get() ) );

                    // System.out.println( stockExchange + " " + mic + " " + region + " " + utc + " " + localOpen + " " + localClosed );

                    Berza berza = new Berza( stockExchange, mic, region, utc, localOpen, localClosed );
                    berza.setValute( valute );

                    this.berzaRepository.save( berza );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importCurrencies()
    {
        try {
            Document doc = Jsoup.connect( "https://en.wikipedia.org/wiki/Template:Most_traded_currencies" ).get();
            Elements table = doc.getElementsByClass( "wikitable" );

            for( Element row: table.get( 0 ).getElementsByTag( "tbody" ).get( 0 ).getElementsByTag( "tr" ) ) {

                Elements columns = row.getElementsByTag( "td" );

                if ( !columns.isEmpty() ) {
                    String currencyName	= columns.get( 1 ).getAllElements().last().text();
                    String isoCode 		= columns.get( 2 ).getAllElements().last().text();
                    Currency currency 	= Currency.getInstance( isoCode );
                    String symbol 		= currency.getSymbol();
                    String location 	= "N/A";

                    /* Malo hardkodovanja. */
                    switch ( isoCode ) {
                        case "AUD":
                            location = "Australia";
                            break;
                        case "CAD":
                            location = "Canada";
                            break;
                        case "CNY":
                            location = "China";
                            break;
                        case "TWD":
                            location = "Taiwan";
                            break;
                        default:
                            try {
                                location = columns.get( 1 ).getElementsByClass( "thumbborder" ).first().attributes().get( "src" );
                                location = location.split("Flag_of_")[1];
                                location = location.split( ".svg" )[0];
                                location = location.replace( "_", " " );
                                location = removePrefix( location, "the " );
                            } catch ( ArrayIndexOutOfBoundsException aiobe ) {}
                    }

                    // System.out.println( currencyName + " " + isoCode + " " + symbol + " " + location );
                    this.valutaRepository.save( new Valuta( currencyName, isoCode, symbol, location ) );

                    /* Poslednja u tabeli. */
                    if( isoCode.equals( "RON" ) ) break;
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
