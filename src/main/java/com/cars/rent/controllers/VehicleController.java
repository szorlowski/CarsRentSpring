package com.cars.rent.controllers;

import com.cars.rent.exceptions.EntityNotFoundException;
import com.cars.rent.models.Vehicle;
import com.cars.rent.repositories.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/vehicles", produces = "application/json")
public class VehicleController {

    private final VehicleRepository repository;
    public VehicleController(VehicleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Vehicle> getAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicle(@PathVariable long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping
    public void createVehicle(@RequestBody Vehicle vehicle){
        repository.save(vehicle);
    }

    @PutMapping("/{id}")
    public void updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle){
        vehicle.setId(id);
        repository.save(vehicle);
    }
}
