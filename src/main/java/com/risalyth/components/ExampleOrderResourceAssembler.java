package com.risalyth.components;

import com.risalyth.controllers.ExampleOrderController;
import com.risalyth.models.ExampleOrder;
import com.risalyth.models.Status;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ExampleOrderResourceAssembler implements ResourceAssembler<ExampleOrder, Resource<ExampleOrder>> {
    @Override
    public Resource<ExampleOrder> toResource(ExampleOrder order) {
        //        Unconditional links to single-item resource and aggregate root

        Resource<ExampleOrder> orderResource = new Resource<>(order,
                linkTo(methodOn(ExampleOrderController.class).getOrder(order.getId())).withSelfRel(),
                linkTo(methodOn(ExampleOrderController.class).getOrders()).withRel("orders"));

        //      Conditional links based on state od the order
        if (order.getStatus() == Status.IN_PROGRESS) {
            orderResource.add(
                    linkTo(methodOn(ExampleOrderController.class).cancel(order.getId())).withRel("cancel"));
            orderResource.add(
                    linkTo(methodOn(ExampleOrderController.class).complete(order.getId())).withRel("complete"));
        }
        return orderResource;
    }
}
