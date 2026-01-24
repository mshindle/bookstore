package dev.irontech.bookstore.order.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.util.List;

/**
 * DTO for retrieving customer orders.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerOrderDto {

    private final Long id;
    private CustomerDto customer;
    private ShippingAddressDto shippingAddress;
    private List<ItemDto> items;

    /**
     * Constructor that maps CustomerOrder entity to DTO.
     *
     * @param customerOrder the CustomerOrder entity to map
     */
    public CustomerOrderDto(CustomerOrder customerOrder) {
        this.id = customerOrder.getId();

        if (customerOrder.getCustomer() != null) {
            this.customer = new CustomerDto(customerOrder.getCustomer());
        }

        this.shippingAddress = new ShippingAddressDto(customerOrder);

        if (customerOrder.getItems() != null) {
            this.items = customerOrder.getItems().stream()
                    .map(ItemDto::new)
                    .toList();
        }
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerDto {
        private final String userId;
        private final String firstName;
        private final String lastName;

        public CustomerDto(Customer customer) {
            this.userId = customer.getUserId();
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
        }
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShippingAddressDto {
        private final String name;
        private final String street;
        private final String city;
        private final String state;
        private final String country;
        private final String postalCode;

        public ShippingAddressDto(CustomerOrder customerOrder) {
            this.name = customerOrder.getReceiverName();
            this.street = customerOrder.getStreet();
            this.city = customerOrder.getCity();
            this.state = customerOrder.getState();
            this.country = customerOrder.getCountry();
            this.postalCode = customerOrder.getPostalCode();
        }
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemDto {
        private final String articleNumber;
        private final Integer quantity;
        private final String title;

        public ItemDto(OrderItem item) {
            this.articleNumber = item.getItem().getArticleNumber();
            this.quantity = item.getQuantity();
            this.title = item.getItem().getTitle();
        }
    }
}