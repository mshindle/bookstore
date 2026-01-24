package dev.irontech.bookstore.order.repository;

import dev.irontech.bookstore.order.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}