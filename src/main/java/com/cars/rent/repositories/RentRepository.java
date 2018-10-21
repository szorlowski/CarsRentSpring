package com.cars.rent.repositories;

import com.cars.rent.models.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {
}
