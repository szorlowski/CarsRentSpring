package com.risalyth.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import com.risalyth.exceptions.UserNotFoundException;
import com.risalyth.models.User;
import com.risalyth.repositories.UserRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    Resources<Resource<User>> allUsers() {
        List<Resource<User>> users = repository.findAll().stream()
                .map(user -> new Resource<>(user, linkTo(methodOn(UserController.class)
                        .getUser(user.getId())).withSelfRel(), linkTo(methodOn(UserController.class).allUsers()).withRel("users")))
                .collect(Collectors.toList());

        return new Resources<>(users,
                linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
    }

    @PostMapping
    User createUser(@RequestBody User user) {
        return repository.save(user);
    }

    @GetMapping("/{id}")
    Resource<User> getUser(@PathVariable Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return new Resource<>(user,
                linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).allUsers()).withRel("employees"));
    }

    @PutMapping("/{id}")
    User replaceEmployee(@RequestBody User user, @PathVariable Long id) {

        return repository.findById(id).map(u -> {
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            return repository.save(u);
        }).orElseGet(() -> {
            user.setId(id);
            return repository.save(user);
        });
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
