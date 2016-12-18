package test2.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Auto {

    String make;
    String model;
    LocalDate date;
    Long price;
    Long mileage;
    FuelType fuelType;
    String location;
    Transmission transmission;
    Double engineCapacity;
    Boolean custom;
    Boolean accidentFree;
    Boolean notInUkraine;


}
