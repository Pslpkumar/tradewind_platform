package com.tradewind.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class OrderApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void placesAnOrderAndFetchesItById() throws Exception {
        String body = """
                {"symbol":"aapl","side":"BUY","quantity":10,"limitPrice":185.50}
                """;

        MvcResult created = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andReturn();

        String location = created.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.side").value("BUY"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void unknownOrderReturns404WithErrorCode() throws Exception {
        mockMvc.perform(get("/api/v1/orders/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value("ORD-404"));
    }

    @Test
    void invalidFieldsReturn400WithFieldErrors() throws Exception {
        String body = """
                {"symbol":"","side":"BUY","quantity":0,"limitPrice":-5}
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ORD-400"))
                .andExpect(jsonPath("$.errors.length()").value(3));
    }

    @Test
    void invalidEnumValueReturns400Malformed() throws Exception {
        String body = """
                {"symbol":"AAPL","side":"HOLD","quantity":10,"limitPrice":185.50}
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ORD-400-MALFORMED"));
    }

    @Test
    void malformedIdReturns400Malformed() throws Exception {
        mockMvc.perform(get("/api/v1/orders/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ORD-400-MALFORMED"));
    }

    @Test
    void unknownUrlIsNotTurnedIntoA500() throws Exception {
        mockMvc.perform(get("/api/v1/nothing-here"))
                .andExpect(status().isNotFound());
    }
}