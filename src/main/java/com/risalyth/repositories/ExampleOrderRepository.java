package com.risalyth.repositories;

import com.risalyth.models.ExampleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleOrderRepository extends JpaRepository<ExampleOrder, Long> {
}
