package com.risalyth.controllers;

import com.risalyth.components.UserResourceAssembler;
import com.risalyth.exceptions.UserNotFoundException;
import com.risalyth.models.ExampleOrder;
import com.risalyth.models.Status;
import com.risalyth.models.User;
import com.risalyth.repositories.UserRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private final UserRepository repository;
    private final UserResourceAssembler assembler;

    public UserController(UserRepository repository, UserResourceAssembler userResourceAssembler) {
        this.repository = repository;
        this.assembler = userResourceAssembler;
    }

    @GetMapping
    public Resources<Resource<User>> allUsers() {

        List<Resource<User>> users = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(users, linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) throws URISyntaxException {
        Resource<User> resource = assembler.toResource(repository.save(user));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping(value = "/{id}")
    public Resource<User> getUser(@PathVariable Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toResource(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody User user, @PathVariable Long id) throws URISyntaxException {

        User updatedUser = repository.findById(id).map(u -> {
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            return repository.save(u);
        }).orElseGet(() -> {
            user.setId(id);
            return repository.save(user);
        });

        Resource<User> resourceUser = assembler.toResource(updatedUser);
        return ResponseEntity
                .created(new URI(resourceUser.getId().expand().getHref()))
                .body(resourceUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
