package dev.irontech.bookstore.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.irontech.bookstore.enums.Events;
import dev.irontech.bookstore.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCreated {
    @Builder.Default
    private final Events eventType = Events.ORDER_CREATED;
    private OrderStatus status;
    private String orderId;
    private String customerId;

    @Builder.Default
    private List<Item> items = new LinkedList<>();

    @Data
    public static class Item {
        private String bookId;
        private Integer quantity;
    }
}
