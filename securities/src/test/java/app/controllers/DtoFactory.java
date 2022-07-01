package app.controllers;

import app.model.*;
import app.model.dto.*;

import java.math.BigDecimal;
import java.util.Date;

public class DtoFactory {

    public static SecurityDTO generatePlainDTO( ModelType modelType ) throws Exception {
        switch( modelType )
        {
            case FOREX:
                Currency baseCurrency =  new Currency();
                baseCurrency.setIsoCode( "EUR" );

                Currency quoteCurrency = new Currency();
                quoteCurrency.setIsoCode( "USD" );

                Forex forex = new Forex();
                forex.setBaseCurrency( baseCurrency );
                forex.setQuoteCurrency( quoteCurrency );

                return new ForexDTO( forex );

            case FUTURE:
                Future future = new Future();
                future.setContractUnit( "futureTest" );
                future.setMaintenanceMargin( new BigDecimal( Math.E ) );
                future.setSettlementDate( new Date( 0 ) );  /* Beginning of the universe :] */

                return new FutureDTO( future );

            case STOCK:
                Stock stock = new Stock();
                stock.setOutstandingShares( ( 123L ) );
                stock.setDividendYield( new BigDecimal( Math.PI ) );

                return new StockDTO( stock );

            case OPTION:
                StockOption option = new StockOption();
                option.setStockListing( new Stock() );
                option.setOptionType( OptionType.CALL );
                option.setStrikePrice( new BigDecimal( Math.E ) );
                option.setImpliedVolatility( new BigDecimal( Math.PI ) );
                option.setOpenInterest( 1L );
                option.setSettlementDate( new Date( 1L ) );

                return new OptionDTO( option );
        }

        throw new Exception( "ModelType does not exist." );
    }

}
