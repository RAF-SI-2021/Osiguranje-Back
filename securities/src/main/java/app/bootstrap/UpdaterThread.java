package app.bootstrap;

import app.services.ForexService;
import app.services.FuturesService;
import app.services.OptionsService;
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
    private final OptionsService optionsService;

    private boolean running = true;

    @Autowired
    public UpdaterThread(StockService stockService, ForexService forexService, FuturesService futuresService, OptionsService optionsService) {
        this.stockService = stockService;
        this.forexService = forexService;
        this.futuresService = futuresService;
        this.optionsService = optionsService;
    }

    @SneakyThrows
    @Override
    public void run() {
        while(running){
            TimeUnit.MINUTES.sleep(1);

            try {
                stockService.updateData();
                forexService.updateData();
                futuresService.updateData();
                optionsService.updateData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean b){
        running = b;
    }
}
