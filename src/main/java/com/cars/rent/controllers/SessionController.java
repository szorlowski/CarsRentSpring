package com.cars.rent.controllers;

import com.cars.rent.models.Customer;
import com.cars.rent.repositories.CustomerRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/login", produces = "application/json")
public class SessionController {

    private final CustomerRepository customerRepository;

    public SessionController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public Customer createSession(@RequestBody Customer customer){
        Customer customer2 = customerRepository.findCustomerByEmail(customer.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        if(customer2.getPassword().equals(customer.getPassword())){
            return customer2;
        }
        throw new EntityNotFoundException();
    }
}
