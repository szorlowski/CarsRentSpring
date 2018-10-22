package com.cars.rent.models;

import com.cars.rent.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "vehicles")
@Data
@Entity
@NoArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private char type;
    @Column
    private String name;
    @Column
    private int price;
    @Column
    private int stars;
    
    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.EAGER)
    private List<Rent> rents;

    private boolean anyDayMatch(List<LocalDate> list) {
        return list.stream().anyMatch(getAllDatesFromRents()::contains);
    }

    private boolean anyDayMatch(List<LocalDate> list, List<LocalDate> list2) {
        return list.stream().anyMatch(list2::contains);
    }

    @JsonIgnore
    public List<LocalDate> getAllDatesFromRents() {
        return rents
                .stream()
                .map(rent -> DateUtils.getDaysBetweenDates(rent.getRentedFrom(), rent.getRentedTo()))
                .flatMap(List::stream)
                .distinct()
                .filter(date -> date.isAfter(LocalDate.now().minusDays(1)))
                .collect(Collectors.toList());
    }
    
    @JsonIgnore
    public boolean isRented(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> rentDays = DateUtils.getDaysBetweenDates(startDate, endDate);
        
        return anyDayMatch(rentDays);
    }
    
    @JsonIgnore
    public boolean isRented(List<LocalDate> days) {
        return days.stream().anyMatch(getAllDatesFromRents()::contains);
    }
    
    public boolean isRented() {
        return isRented(Collections.singletonList(LocalDate.now()));
    }
}
