package com.risalyth.controllers;

import com.risalyth.components.ExampleOrderResourceAssembler;
import com.risalyth.exceptions.ExampleOrderNotFoundException;
import com.risalyth.models.ExampleOrder;
import com.risalyth.models.Status;
import com.risalyth.repositories.ExampleOrderRepository;
import org.hibernate.criterion.Example;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/orders", produces = "application/json")
public class ExampleOrderController {

    private final ExampleOrderRepository repository;
    private final ExampleOrderResourceAssembler assembler;

    public ExampleOrderController(ExampleOrderRepository repository, ExampleOrderResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    public Resources<Resource<ExampleOrder>> getOrders(){
        List<Resource<ExampleOrder>> orders = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(orders,
                linkTo(methodOn(ExampleOrderController.class).getOrders()).withSelfRel());
    }

    @GetMapping("/{id}")
    public Resource<ExampleOrder> getOrder(@PathVariable long id){
        return assembler.toResource(
                repository.findById(id)
                .orElseThrow(() -> new ExampleOrderNotFoundException(id))
        );
    }

    @PostMapping
    public ResponseEntity<Resource<ExampleOrder>> createOrder(@RequestBody ExampleOrder order){
        order.setStatus(Status.IN_PROGRESS);
        ExampleOrder newOrder = repository.save(order);
        return ResponseEntity
                .created(linkTo(methodOn(ExampleOrderController.class).getOrder(newOrder.getId())).toUri())
                .body(assembler.toResource(newOrder));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<ResourceSupport> cancel(@PathVariable Long id){
        ExampleOrder order = repository.findById(id).orElseThrow(() -> new ExampleOrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toResource(repository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ResourceSupport> complete(@PathVariable Long id){
        ExampleOrder order = repository.findById(id).orElseThrow(() -> new ExampleOrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toResource(repository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
    }
}
