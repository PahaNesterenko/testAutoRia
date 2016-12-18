package test2.domain;

public enum Transmission {

    MANUAL(1, "Manual"),
    SEMI_AUTOMATIC(2, "Semi-automatic"),
    AUTOMATIC(3, "Automatic"),
    VARIATOR(4, "Variator");

    Integer id;
    String name;
    Transmission(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
