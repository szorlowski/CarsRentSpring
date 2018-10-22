package com.cars.rent.controllers;

import com.cars.rent.exceptions.EntityNotFoundException;
import com.cars.rent.exceptions.RentIsActiveException;
import com.cars.rent.models.Rent;
import com.cars.rent.models.Vehicle;
import com.cars.rent.repositories.RentRepository;
import com.cars.rent.repositories.VehicleRepository;
import com.cars.rent.utils.DateUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rents", produces = "application/json")
public class RentController {
    private final RentRepository rentRepository;
    private final VehicleRepository vehicleRepository;

    public RentController(RentRepository rentRepository, VehicleRepository vehicleRepository) {
        this.rentRepository = rentRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public List<Rent> allRents(){
        return rentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Rent getRent(@PathVariable long id){
        return rentRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @PostMapping
    public void createRent(@RequestBody Rent rent){
        Vehicle vehicle = vehicleRepository.findById(rent.getVehicle().getId()).get();

        List<LocalDate> days = DateUtils.getDaysBetweenDates(rent.getRentedFrom(), rent.getRentedTo());
        List<LocalDate> takenDays = vehicle.getAllDatesFromRents();
        List<LocalDate> rentedIn = days
                .stream()
                .filter(takenDays::contains)
                .collect(Collectors.toList());

        if(rentedIn.size() != 0){
            throw new RentIsActiveException(rentedIn);
        }
        rentRepository.save(rent);
    }

    @DeleteMapping("/{id}")
    public void deleteRent(@PathVariable Long id) {
        rentRepository.deleteById(id);
    }

    @DeleteMapping("/all")
    public void deleteAll(){
        rentRepository.deleteAll();
    }


    @PutMapping("/{id}")
    public void updateRent(@PathVariable long id, @RequestBody Rent rent){
        Vehicle vehicle = vehicleRepository.findById(rent.getVehicle().getId()).get();
        Rent prevRent = getRent(id);

        List<LocalDate> prevDays = DateUtils.getDaysBetweenDates(prevRent.getRentedFrom(), prevRent.getRentedTo());
        List<LocalDate> newDays = DateUtils.getDaysBetweenDates(rent.getRentedFrom(), rent.getRentedTo());

        List<LocalDate> differentDays = newDays.stream().filter(d -> !prevDays.contains(d)).collect(Collectors.toList());

        rent.setId(id);
        if(vehicle.isRented(differentDays)){
            throw new RentIsActiveException(differentDays);
        }
        rentRepository.save(rent);
    }
}
