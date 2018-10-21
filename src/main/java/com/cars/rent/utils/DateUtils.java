package com.cars.rent.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtils {

    public static List<LocalDate> getDaysBetweenDates(LocalDate from, LocalDate to){
        return Stream
                .iterate(from, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(from, to))
                .collect(Collectors.toList());
    }
}
