package com.cars.rent.exceptions;

import java.time.LocalDate;
import java.util.List;

public class RentIsActiveException extends RuntimeException {
    public RentIsActiveException() {
        super("Vehicle is taken in at least one selected day!");
    }

    public RentIsActiveException(List<LocalDate> list) {
        super("Vehicle is taken in following days:" + list);
    }
}
