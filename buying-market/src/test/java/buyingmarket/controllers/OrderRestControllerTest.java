package buyingmarket.controllers;

import buyingmarket.model.SecurityType;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {OrderRestController.class})
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void createOrderTest() throws Exception {
        OrderDto orderDto = OrderDto.builder()
                .active(Boolean.TRUE)
                .orderId(Long.valueOf(1))
                .securityId(Long.valueOf(1))
                .userId(Long.valueOf(1))
                .amount(Integer.valueOf(234))
                .securityType(SecurityType.STOCKS)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String orderDtoJson = writer.writeValueAsString(orderDto);
        mockMvc.perform(post("/api/orders").header("Authorization","").content(orderDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void findAllTest() throws Exception {
        when(orderService.findAllOrdersForUser("")).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/orders").header("Authorization", ""))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void findOrderTest() throws Exception {
        OrderDto orderDto = OrderDto.builder()
                .active(Boolean.TRUE)
                .orderId(Long.valueOf(1))
                .build();
        when(orderService.findOrderForUser(Long.valueOf(1), "")).thenReturn(orderDto);
        mockMvc.perform(get("/api/orders/1").header("Authorization", ""))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void updateOrderTest() throws Exception {
        OrderDto orderDto = OrderDto.builder()
                .active(Boolean.TRUE)
                .orderId(Long.valueOf(1))
                .securityId(Long.valueOf(1))
                .userId(Long.valueOf(1))
                .amount(Integer.valueOf(234))
                .securityType(SecurityType.STOCKS)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String orderDtoJson = writer.writeValueAsString(orderDto);
        mockMvc.perform(put("/api/orders").header("Authorization","").content(orderDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void deleteOrderTest() throws Exception {
        mockMvc.perform(delete("/api/orders/1").header("Authorization",""))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void deleteAllTest() throws Exception {
        mockMvc.perform(delete("/api/orders").header("Authorization",""))
                .andDo(print()).andExpect(status().isOk());
    }
}
