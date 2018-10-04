package com.risalyth.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "example_orders")
public class ExampleOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String description;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public ExampleOrder(String description, Status status) {
        this.description = description;
        this.status = status;
    }

    public ExampleOrder() {
    }
}
