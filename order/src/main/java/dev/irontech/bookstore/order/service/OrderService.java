package dev.irontech.bookstore.order.service;

import dev.irontech.bookstore.order.models.CreateCustomerOrderDto;
import dev.irontech.bookstore.order.models.Customer;
import dev.irontech.bookstore.order.models.CustomerOrder;
import dev.irontech.bookstore.order.models.CustomerOrderDto;
import dev.irontech.bookstore.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final CustomerService customerService;
    private final ItemService itemService;

    /**
     * Creates a new order.
     * @param orderData - the order data.
     * @return the order ID.
     */
    @Transactional
    public Long createOrder(CreateCustomerOrderDto orderData) {
        CustomerOrder order = new CustomerOrder();

        // Check if a Customer exists, if not create new one
        Customer customer = customerService.getOrCreateCustomer(orderData.getUserId(), orderData.getCustomer());

        // Save the order
        order.setReceiverName(orderData.getShippingAddress().getName());
        order.setStreet(orderData.getShippingAddress().getStreet());
        order.setCity(orderData.getShippingAddress().getCity());
        order.setState(orderData.getShippingAddress().getState());
        order.setCountry(orderData.getShippingAddress().getCountry());
        order.setPostalCode(orderData.getShippingAddress().getPostalCode());
        order.setCustomer(customer);
        order = orderRepository.save(order);

        // Create the order items
        itemService.createItems(orderData.getItems(), order);

        return order.getId();
    }

    public Optional<CustomerOrderDto> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(CustomerOrderDto::new);
    }

    /**
     * Gets all orders.
     * @return the list of orders.
     */
    public List<CustomerOrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(CustomerOrderDto::new)
                .toList();
    }

    @Transactional
    public void deleteOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

}
