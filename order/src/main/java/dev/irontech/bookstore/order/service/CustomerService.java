package dev.irontech.bookstore.order.service;

import dev.irontech.bookstore.order.models.CreateCustomerOrderDto;
import dev.irontech.bookstore.order.models.Customer;
import dev.irontech.bookstore.order.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Customer getOrCreateCustomer(String userId, CreateCustomerOrderDto.CustomerDto customerDto) {
        return customerRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setUserId(userId);
                    newCustomer.setFirstName(customerDto.getFirstName());
                    newCustomer.setLastName(customerDto.getLastName());
                    return customerRepository.save(newCustomer);
                });
    }

}
