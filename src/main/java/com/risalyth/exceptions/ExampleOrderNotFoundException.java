package com.risalyth.exceptions;

public class ExampleOrderNotFoundException extends RuntimeException {
    public ExampleOrderNotFoundException(long id) {
        super("Example Order not found with id: " + id);
    }
}
