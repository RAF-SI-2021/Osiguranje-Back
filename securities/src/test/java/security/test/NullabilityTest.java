package security.test;


import app.App;
import controllers.Controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import services.ForexService;
import services.FuturesService;
import services.StockService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
@ActiveProfiles("test")
public class NullabilityTest {

    @Autowired
    private Controller controller;

    @Autowired
    private StockService stockService;

    @Autowired
    private FuturesService futuresService;

    @Autowired
    private ForexService forexService;


    @Test
    void testFirmness(){
        assertThat(stockService).isNotNull();
        assertThat(futuresService).isNotNull();
        assertThat(forexService).isNotNull();
        assertThat(controller).isNotNull();

    }

}
