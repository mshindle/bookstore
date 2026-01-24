package dev.irontech.bookstore.order.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * DTO for creating an order.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCustomerOrderDto {

    // TODO: remove from DTO when user service is implemented
    //  and get the user id within authentication context
    @JsonProperty(required = true)
    private String userId;
    private CustomerDto customer;
    @JsonProperty(required = true)
    private ShippingAddressDto shippingAddress;
    private List<ItemDto> items;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShippingAddressDto {
        @JsonProperty(required = true)
        private String name;
        @JsonProperty(required = true)
        private String street;
        @JsonProperty(required = true)
        private String city;
        @JsonProperty(required = true)
        private String state;
        @JsonProperty(required = true)
        private String country;
        @JsonProperty(required = true)
        private String postalCode;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemDto {
        @JsonProperty(required = true)
        private String articleNumber;

        @JsonProperty(required = true)
        private Integer quantity;

        @JsonProperty(required = true)
        private String title;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerDto {
        @JsonProperty(required = true)
        private String firstName;
        @JsonProperty(required = true)
        private String lastName;
    }
}