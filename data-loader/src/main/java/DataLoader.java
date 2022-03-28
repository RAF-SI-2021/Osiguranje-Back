package main.java;

import main.java.models.Berza;
import main.java.models.Valuta;
import main.java.repositories.BerzaRepository;
import main.java.repositories.ValutaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

@SpringBootApplication
public class DataLoader implements CommandLineRunner {

    private final String VALUTE_CSV = "./data-loader/src/main/resources/data/valute.csv";
    private final String BERZE_CSV  = "./data-loader/src/main/resources/data/berze.csv";

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

        } catch (IOException e) {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String removePrefix( String s, String prefix ) {
        if( s.startsWith( prefix ) )
            return s.substring( prefix.length() );

        return s;
    }
}
