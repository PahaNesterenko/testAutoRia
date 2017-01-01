package test2.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

@Data
public class Auto {

    String make;
    String model;
    Year year;
    Long price;
    Long mileage;
    FuelType fuelType;
    String location;
    Transmission transmission;
    Double engineCapacity;
    Boolean customRegistered = true;
    Boolean accidentFree = true;
    Boolean locatedInUkraine = true;


}
