package dev.irontech.bookstore.order.repository;

import dev.irontech.bookstore.order.models.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}
