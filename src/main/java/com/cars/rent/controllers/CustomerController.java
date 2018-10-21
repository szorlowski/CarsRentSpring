package com.cars.rent.controllers;

import com.cars.rent.exceptions.EntityNotFoundException;
import com.cars.rent.models.Customer;
import com.cars.rent.repositories.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customers", produces = "application/json")
public class CustomerController {
    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Customer> allCustomers(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @PostMapping
    public void createCustomer(@RequestBody Customer customer){
        repository.save(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        customer.setId(id);
        repository.save(customer);
    }
}
