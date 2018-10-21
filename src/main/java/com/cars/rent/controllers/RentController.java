package com.cars.rent.controllers;

import com.cars.rent.exceptions.EntityNotFoundException;
import com.cars.rent.models.Rent;
import com.cars.rent.repositories.RentRepository;
import com.cars.rent.repositories.VehicleRepository;
import com.cars.rent.exceptions.RentIsActiveException;
import com.cars.rent.models.Vehicle;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        if(vehicle.isRented(rent.getRentedFrom(), rent.getRentedTo())){
            throw new RentIsActiveException();
        }
        rentRepository.save(rent);
    }

    @DeleteMapping("/{id}")
    public void deleteRent(@PathVariable Long id) {
        rentRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void updateRent(@PathVariable long id, @RequestBody Rent rent){
        rent.setId(id);
        rentRepository.save(rent);
    }
}
