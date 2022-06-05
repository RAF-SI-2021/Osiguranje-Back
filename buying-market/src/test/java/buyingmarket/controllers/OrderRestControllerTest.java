package buyingmarket.controllers;

import buyingmarket.model.SecurityType;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {OrderRestController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {
    @Autowired
    private OrderRestController orderRestController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void createOrderTest() {

    }

    @Test
    public void findAllTest() throws Exception {
        when(orderService.findAllOrdersForUser("")).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/orders").header("Authorization", ""))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void findOrderTest() {

    }

    @Test
    public void updateOrderTest() {

    }

    @Test
    public void deleteOrderTest() {

    }

    @Test
    public void deleteAllTest() {

    }
}
