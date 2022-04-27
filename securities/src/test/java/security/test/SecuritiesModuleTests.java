package security.test;

import app.App;
import app.controllers.Controller;
import app.model.*;
import app.model.dto.DataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import app.services.ForexService;
import app.services.FuturesService;
import app.services.StockService;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecuritiesModuleTests {

    @Autowired
    private Controller controller;

    @Autowired
    private StockService stockService;

    @Autowired
    private FuturesService futuresService;

    @Autowired
    private ForexService forexService;

    private List<Stock> getStocks(){

        List<Stock> toReturn = new ArrayList<>();

        Region region = new Region("Serbia", "SRB");
        Exchange e = new Exchange("Belgrade Stock Exchange", "BSE", "XBES", "0", "9", "15");
        e.setRegion(region);

        toReturn.add(new Stock("TT1","",e,"09/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L, new BigDecimal(2)));
        toReturn.add(new Stock("TT2","",e,"10/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L,  new BigDecimal(2)));
        toReturn.add(new Stock("TT3","",e,"12/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L,  new BigDecimal(2)));
        toReturn.add(new Stock("TT4","",e,"14/10/2022",new BigDecimal(15),new BigDecimal(15),new BigDecimal(15),new BigDecimal(1),100L,100L,  new BigDecimal(2)));

        return toReturn;
    }

    private List<Forex> getForex(){
        List<Forex> toReturn = new ArrayList<>();
        Region serbia = new Region("Serbia", "SRB");
        Region croatia = new Region("Croatia", "CRO");

        Currency dinar = new Currency("Dinar", "RSD", "rsd", serbia);
        Currency kuna = new Currency("Kuna", "HRK", "kn", croatia);
        Currency runa = new Currency("Runa", "HRR", "rn", croatia);

        toReturn.add(new Forex("FF1","","20/02/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L, dinar,runa,1000));
        toReturn.add(new Forex("FF2","","21/02/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L, dinar,runa,1000));
        toReturn.add(new Forex("FF3","","23/02/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,dinar,runa,2000));
        toReturn.add(new Forex("FF4","","20/03/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,dinar,runa,3000));
        toReturn.add(new Forex("FF5","","20/03/2021",new BigDecimal(180),new BigDecimal(180),new BigDecimal(180),new BigDecimal(1821),143L,dinar,kuna,3000));

        return toReturn;
    }

    private List<Future> getFuture(){
        List<Future> toReturn = new ArrayList<>();

        toReturn.add(new Future("TF1","","12/1/2019",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));
        toReturn.add(new Future("TF2","","13/1/2019",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));
        toReturn.add(new Future("TF3","","14/2/2019",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));
        toReturn.add(new Future("TF4","","12/1/2020",new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),new BigDecimal(14),100L,12,"$",10,new Date(2023,1,15)));

        return toReturn;
    }

    @BeforeAll
    void fillDataBase(){
        stockService.saveAll(getStocks());
        getFuture().forEach( x -> futuresService.save(x));
        forexService.saveAll(getForex());
    }

    @Test
    void testStockService() {
        assertThat(stockService).isNotNull();

        List<Stock> result = stockService.findByTicker("TT1");

        Assertions.assertEquals(1,result.size());
        assertThat(result.get(0).getLastUpdated().equals("09/10/2022")).isTrue();
    }

    @Test
    void testFutureService(){
        assertThat(futuresService).isNotNull();

        List<Future> result = futuresService.getFuturesData();

        Assertions.assertEquals(4,result.size());
        assertThat(result.get(0).getTicker().equals("TF1")).isTrue();
        assertThat(result.get(2).getSettlementDate().compareTo(new Date(2023,1,15))).isEqualTo(0);
    }

    @Test
    void testForexService(){
        assertThat(forexService).isNotNull();

        List<Forex> result = forexService.findByTicker("FF2");

        Assertions.assertEquals(1,result.size());

        assertThat(result.get(0).getLastUpdated().equals("21/02/2021")).isTrue();
    }

//    @Test
//    void testForexServicePair(){
//        Currency dinar = new Currency("Dinar", "RSD", "rsd", "Serbia");
//        Currency kuna = new Currency("Kuna", "HRK", "kn", "Croatia")
//        Currency runa = new Currency("Runa", "HRR", "rn", "Croatia")
//
//        Assertions.assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
//            forexService.getPair(dinar,runa);
//        });
//
//        Forex forex = forexService.getPair("Dinar","Kuna");
//        Assertions.assertNotNull(forex);
//    }

    @Test
    void testController(){
        assertThat(controller).isNotNull();

        ResponseEntity<?> responseEntity = controller.getData();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(DataDTO.class);

        DataDTO result = (DataDTO) responseEntity.getBody();

        assertThat(result).isNotNull();
        Assertions.assertEquals(4,result.getFutures().size());
        Assertions.assertEquals(5,result.getForex().size());
        Assertions.assertEquals(4,result.getStock().size());

    }


}
