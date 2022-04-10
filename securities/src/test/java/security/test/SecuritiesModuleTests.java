package security.test;

import app.App;
import model.Forex;
import model.Future;
import model.Stock;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import services.ForexService;
import services.FuturesService;
import services.StockService;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SecuritiesModuleTests {

    @Autowired
    private StockService stockService;

    @Autowired
    private FuturesService futuresService;

    @Autowired
    private ForexService forexService;


    @Test
    void testStockService(){
        stockService.save(new Stock("TT1","","09/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L));
        stockService.save(new Stock("TT2","","10/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L));
        stockService.save(new Stock("TT3","","12/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L));
        stockService.save(new Stock("TT4","","14/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L));

        List<Stock> result = stockService.findBySymbol("TT1");

        Assertions.assertEquals(1,result.size());
        assertThat(result.get(0).getLastUpdated().equals("09/10/2022")).isTrue();


    }

    @Test
    void testFutureService(){
        futuresService.save(new Future("TF1","","12/1/2019",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));
        futuresService.save(new Future("TF2","","13/1/2019",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));
        futuresService.save(new Future("TF3","","14/2/2019",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));
        futuresService.save(new Future("TF1","","12/1/2020",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));

        List<Future> result = futuresService.getFuturesData();

        Assertions.assertEquals(4,result.size());
        assertThat(result.get(0).getSymbol().equals("TF1")).isTrue();
        System.out.println(result.get(2).getSettlementDate());
        assertThat(result.get(2).getSettlementDate().compareTo(new Date(2023,1,15))).isEqualTo(0);

    }

    @Test
    void testForexService(){

        forexService.save(new Forex("FF1","","20/02/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,"Dinar","Runa",1000));
        forexService.save(new Forex("FF2","","21/02/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,"Dinar","Runa",1000));
        forexService.save(new Forex("FF2","","23/02/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,"Dinar","Runa",2000));
        forexService.save(new Forex("FF4","","20/03/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,"Dinar","Runa",3000));
        forexService.save(new Forex("FF4","","20/03/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,"Dinar","Kuna",3000));


        List<Forex> result = forexService.findBySymbol("FF2");

        Assertions.assertEquals(2,result.size());
        System.out.println(result.get(1).getLastUpdated());
        assertThat(result.get(1).getLastUpdated().equals("23/02/2021")).isTrue();

        Assertions.assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            forexService.getPair("Dinar","Runa");
        });

        Forex forex = forexService.getPair("Dinar","Kuna");
        Assertions.assertNotNull(forex);



    }


}
