package security.test;

import app.model.Currency;
import app.model.Forex;
import app.model.Region;
import app.model.dto.ForexDTO;
import app.services.RedisCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisCacheService.class)
public class RedisCacheServiceIntegration
{
    private RedisCacheService underTest;

    @Autowired
    private Environment env;

    @Test
    public void saveForexDto_getForexDto() {
        underTest = new RedisCacheService(env);

        String forexId = "testId";

        Region testRegion = new Region("testRegion", "regionCode");

        Currency baseCurrency = new Currency("curr", "iso", "TEST", testRegion);
        Currency quotaCurrency = new Currency("curr2", "iso2", "TEST2", testRegion);

        ForexDTO forex = new ForexDTO(new Forex("TEST", "", "", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, 1000L, baseCurrency, quotaCurrency, 10));
        underTest.saveForex(forexId, forex);

        ForexDTO cachedForex = underTest.getForex(forexId);

        assertEquals(forex, cachedForex);
        underTest.deleteForex(forexId);
    }
}
