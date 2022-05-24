package app.bootstrap;

import app.services.ForexService;
import app.services.FuturesService;
import app.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class UpdaterThread implements Runnable {

    private final StockService stockService;
    private final ForexService forexService;
    private final FuturesService futuresService;

    @Autowired
    public UpdaterThread(StockService stockService, ForexService forexService, FuturesService futuresService) {
        this.stockService = stockService;
        this.forexService = forexService;
        this.futuresService = futuresService;
    }

    @Override
    public void run() {
        while(true){
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                stockService.updateData();
                forexService.updateData();
                futuresService.updateData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
