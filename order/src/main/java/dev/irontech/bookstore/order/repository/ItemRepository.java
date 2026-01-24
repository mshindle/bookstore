package dev.irontech.bookstore.order.repository;

import dev.irontech.bookstore.order.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByArticleNumber(String articleNumber);
}