package dev.irontech.bookstore.order;

import dev.irontech.bookstore.enums.OrderStatus;
import dev.irontech.bookstore.events.OrderCreated;
import dev.irontech.bookstore.order.models.CreateCustomerOrderDto;
import dev.irontech.bookstore.order.models.CustomerOrderDto;
import dev.irontech.bookstore.order.models.IdempotencyKey;
import dev.irontech.bookstore.order.service.IdempotencyKeyService;
import dev.irontech.bookstore.order.service.OrderService;
import jakarta.persistence.RollbackException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
// Starts with "/" for all incoming requests, since the gateway service is configured to forward all "api/v1/orders" requests to this service"
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {
    public static final String IDEMPOTENCY_KEY_HEADER = "X-Idempotency-Key";
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
//    private final KafkaProducerService kafkaProducerService;
    private final OrderService orderService;
    private final IdempotencyKeyService idempotencyKeyService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(
            @RequestHeader(IDEMPOTENCY_KEY_HEADER) String idempotencyKeyHeader,
            @RequestBody CreateCustomerOrderDto orderData) {

        // Log the order information
        logger.info("Processing order creation request");
        IdempotencyKey idempotencyKey;
        try {
            idempotencyKey = idempotencyKeyService.getOrCreateIdempotencyKeyByKeyAndUserId(
                    idempotencyKeyHeader,
                    orderData.getUserId());
        } catch (IllegalStateException | RollbackException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of(
                            "message", "Duplicate order request detected. This request has already been processed.",
                            "status", "duplicate"
                    )
            );
        }

        logger.atInfo().addKeyValue("key",idempotencyKey.getIdempotencyKey()).log("idempotency key applied");
        Long orderId = orderService.createOrder(orderData);

        // Emit Kafka event
        OrderCreated eventDto = OrderCreated.builder()
                .orderId(orderId.toString())
                .items(orderData.getItems().stream().map(item -> {
                    OrderCreated.Item eventItem = new OrderCreated.Item();
                    eventItem.setBookId(item.getArticleNumber());
                    eventItem.setQuantity(item.getQuantity());
                    return eventItem;
                }).toList())
                .status(OrderStatus.CREATED)
                .customerId(orderData.getUserId())
                .build();

//        kafkaProducerService.sendMessage("orders-topic", eventDto);
//        logger.info("Kafka event emitted to orders-topic");

        // Return a simple response
        Map<String, String> response = Map.of(
                "orderId", orderId.toString(),
                "message", "Order request received, logged and Kafka event emitted successfully",
                "status", "processed"
        );

        logger.info("Order creation request processed successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerOrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
