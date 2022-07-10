package buyingmarket.controllers;

import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.services.ActuaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith( MockitoExtension.class )
public class ActuaryRestControllerTest {

    @Mock
    private ActuaryService actuaryService;

    private ActuaryRestController underTest;

    @BeforeEach
    void setUp() {
        underTest = new ActuaryRestController(actuaryService);
    }

    @Test
    public void createActuaryTest(){

        ActuaryCreateDto actuaryCreateDto = new ActuaryCreateDto();


        ResponseEntity<?> result = underTest.createActuary( actuaryCreateDto );

        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void setUsedLimitTest(){
        final long ID = 10;

        ResponseEntity<?> result = underTest.setUsedLimit( ID );

        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void setUsedTest(){
        final long ID = 10;
        final BigDecimal LIMIT = new BigDecimal(999);
        ResponseEntity<?> result = underTest.setLimit( ID, LIMIT );

        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);

    }

    @Test
    public void updateLimitTest(){
        final long ID = 10;
        final BigDecimal LIMIT = new BigDecimal(999);
        ResponseEntity<?> result = underTest.updateLimit( ID, LIMIT );

        assertEquals(result.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void updateLimitTestException(){
        final long ID = 10;
        final BigDecimal LIMIT = new BigDecimal(999);

        try {
            Mockito.doThrow(new Exception("Limit overflow less then zero")).when(actuaryService).changeLimit(ID,LIMIT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<?> result = underTest.updateLimit( ID, LIMIT );

        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Object body = result.getBody();
        assertTrue(body instanceof String);

    }


}
