package com.tradewind.order_service;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Path;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class OrderContractTest {

    // The hand-written contract file, read straight from docs/. Maven runs tests
    // from backend/order-service, so ../../docs reaches the repo-level docs folder.
    private static final String SPEC =
            Path.of("../../docs/api/order-service/openapi.yml").toUri().toString();

    // Same contract, but request errors are ignored. Used when we send a
    // deliberately invalid request and only want to check the error RESPONSE.
    private static final OpenApiInteractionValidator RESPONSE_ONLY =
            OpenApiInteractionValidator.createFor(SPEC)
                    .withLevelResolver(LevelResolver.create()
                            .withLevel("validation.request", ValidationReport.Level.IGNORE)
                            .build())
                    .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void placeAndGetOrderMatchTheContract() throws Exception {
        String body = """
                {"symbol":"AAPL","side":"BUY","quantity":10,"limitPrice":185.50}
                """;

        MvcResult created = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(openApi().isValid(SPEC))
                .andReturn();

        String location = created.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(openApi().isValid(SPEC));
    }

    @Test
    void unknownOrderMatchesTheContract() throws Exception {
        mockMvc.perform(get("/api/v1/orders/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(openApi().isValid(SPEC));
    }

    @Test
    void invalidBodyMatchesTheContract() throws Exception {
        String body = """
                {"symbol":"","side":"BUY","quantity":0,"limitPrice":-1}
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(openApi().isValid(RESPONSE_ONLY));
    }
}