package com.cars.rent.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id) {
        super("Could not find entity with id: " + id);
    }
}
