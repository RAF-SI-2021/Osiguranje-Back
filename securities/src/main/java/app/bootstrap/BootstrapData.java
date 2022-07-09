package app.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final StocksBootstrap stocksBootstrap;
    private final FuturesBootstrap futuresBootstrap;
    private final ForexBootstrap forexBootstrap;
    private final ExchangeBootstrap exchangeBootstrap;
    private final CurrencyBootstrap currencyBootstrap;
    private final OptionsBootstrap optionsBootstrap;
    private final UpdaterThread updaterThread;

    @Autowired
    public BootstrapData(StocksBootstrap stocksBootstrap, FuturesBootstrap futuresBootstrap, ForexBootstrap forexBootstrap, ExchangeBootstrap exchangeBootstrap, CurrencyBootstrap currencyBootstrap, OptionsBootstrap optionsBootstrap, UpdaterThread updaterThread) {
        this.stocksBootstrap = stocksBootstrap;
        this.futuresBootstrap = futuresBootstrap;
        this.forexBootstrap = forexBootstrap;
        this.exchangeBootstrap = exchangeBootstrap;
        this.currencyBootstrap = currencyBootstrap;
        this.optionsBootstrap = optionsBootstrap;
        this.updaterThread = updaterThread;
    }

    @Override
    public void run(String... args) throws Exception {
        currencyBootstrap.loadCurrenciesData();
        exchangeBootstrap.loadStockExchangeData();
        stocksBootstrap.loadStocksData();
//        optionsBootstrap.loadOptionsData();
        forexBootstrap.loadForexData();
        futuresBootstrap.loadFuturesData();

//        Thread updater = new Thread(updaterThread);
//        updater.start();
    }

}
