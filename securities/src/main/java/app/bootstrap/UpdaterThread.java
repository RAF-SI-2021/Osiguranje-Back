package app.bootstrap;

import app.services.ForexService;
import app.services.FuturesService;
import app.services.StockService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class UpdaterThread implements Runnable {

    private final StockService stockService;
    private final ForexService forexService;
    private final FuturesService futuresService;

    private boolean running = true;

    @Autowired
    public UpdaterThread(StockService stockService, ForexService forexService, FuturesService futuresService) {
        this.stockService = stockService;
        this.forexService = forexService;
        this.futuresService = futuresService;
    }

    @SneakyThrows
    @Override
    public void run() {
        while(running){
            TimeUnit.MINUTES.sleep(15);

            try {
                stockService.updateData();
                forexService.updateData();
                futuresService.updateData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean b){
        running = b;
    }
}
