package com.cars.rent.exceptions;

public class RentIsActiveException extends RuntimeException {
    public RentIsActiveException() {
        super("Vehicle is taken in at least one selected day!");
    }
}
