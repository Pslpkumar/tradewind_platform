package com.tradewind.order_service;
import com.tradewind.order_service.repository.OrderRepository;
import com.tradewind.order_service.entity.Order;
import com.tradewind.order_service.enums.OrderStatus;
import com.tradewind.order_service.enums.Side;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    @Test
    void savesAndFindsOrder() {
        Order saved = repository.save(new Order("AAPL", Side.BUY, 10, new BigDecimal("185.50")));

        Order found = repository.findById(saved.getId()).orElseThrow();

        assertThat(found.getSymbol()).isEqualTo("AAPL");
        assertThat(found.getSide()).isEqualTo(Side.BUY);
        assertThat(found.getLimitPrice()).isEqualByComparingTo("185.50");
        assertThat(found.getStatus()).isEqualTo(OrderStatus.NEW);
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    void databaseRejectsNonPositiveQuantity() {
        Order invalid = new Order("AAPL", Side.BUY, 0, new BigDecimal("1.00"));

        assertThatThrownBy(() -> repository.saveAndFlush(invalid))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}