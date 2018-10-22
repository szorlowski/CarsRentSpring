package com.cars.rent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
@NoArgsConstructor
public class Customer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column
    String email;
    @Column
    String password;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Rent> rents;
}
