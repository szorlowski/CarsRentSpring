package com.cars.rent.controllers;

import com.cars.rent.exceptions.EntityNotFoundException;
import com.cars.rent.models.Vehicle;
import com.cars.rent.repositories.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/vehicles", produces = "application/json")
public class VehicleController {

    private final VehicleRepository repository;

    public VehicleController(VehicleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Vehicle> getAll(@RequestParam(value = "type", required = false) Character type,
                                @RequestParam(value = "price", required = false) Integer price) {
        
        List<Vehicle> list = repository.findAll().stream()
                .sorted(Comparator.comparingInt(Vehicle::getPrice).reversed())
                .collect(Collectors.toList());
        
        if(type != null){
            list = list.stream().filter(v -> v.getType() == Character.toUpperCase(type))
                    .collect(Collectors.toList());
        }
        
        if(price != null){
            list = list.stream().filter(v -> v.getPrice() == price)
                    .collect(Collectors.toList());
        }
        return list;
    }

    @GetMapping("/{id}")
    public Vehicle getVehicle(@PathVariable long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping
    public void createVehicle(@RequestBody Vehicle vehicle) {
        repository.save(vehicle);
    }

    @PutMapping("/{id}")
    public void updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        vehicle.setId(id);
        repository.save(vehicle);
    }
}
