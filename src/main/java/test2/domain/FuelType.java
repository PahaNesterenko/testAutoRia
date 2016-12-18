package test2.domain;

public enum FuelType {

    PETROL(1, "Petrol"),
    DIESEL(2, "Diesel"),
    GASPETROL(3, "Gas/Petrol"),
    ELECTRIC(4, "Electric");

    Integer id;
    String name;

    FuelType(Integer id, String name){
        this.id = id;
        this.name = name;
    }

}
