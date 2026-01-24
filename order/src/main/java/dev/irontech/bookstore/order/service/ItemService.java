package dev.irontech.bookstore.order.service;

import dev.irontech.bookstore.order.models.CreateCustomerOrderDto;
import dev.irontech.bookstore.order.models.CustomerOrder;
import dev.irontech.bookstore.order.models.Item;
import dev.irontech.bookstore.order.models.OrderItem;
import dev.irontech.bookstore.order.repository.ItemRepository;
import dev.irontech.bookstore.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderItem> createItems(List<CreateCustomerOrderDto.ItemDto> itemDtos, final CustomerOrder order) {
        return itemDtos.stream()
                .map(itemDto -> {
                            Item item = itemRepository.findByArticleNumber(itemDto.getArticleNumber())
                                    .orElseGet(() -> {
                                        Item newItem = new Item();
                                        newItem.setTitle(itemDto.getTitle());
                                        newItem.setArticleNumber(itemDto.getArticleNumber());
                                        return itemRepository.save(newItem);
                                    });

                            OrderItem orderItem = new OrderItem();
                            orderItem.setQuantity(itemDto.getQuantity());
                            orderItem.setItem(item);
                            orderItem.setOrder(order);

                            return orderItemRepository.save(orderItem);
                        }

                )
                .toList();
    }
}