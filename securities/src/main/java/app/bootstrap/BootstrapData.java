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

    @Autowired
    public BootstrapData(StocksBootstrap stocksBootstrap, FuturesBootstrap futuresBootstrap, ForexBootstrap forexBootstrap, ExchangeBootstrap exchangeBootstrap) {
        this.stocksBootstrap = stocksBootstrap;
        this.futuresBootstrap = futuresBootstrap;
        this.forexBootstrap = forexBootstrap;
        this.exchangeBootstrap = exchangeBootstrap;
    }

    @Override
    public void run(String... args) throws Exception {
        forexBootstrap.loadForexData();
        exchangeBootstrap.loadStockExchangeData();
        stocksBootstrap.loadStocksData();
        futuresBootstrap.loadFuturesData();
    }

}
