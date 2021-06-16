package com.example.application;

public enum MeasurementDetails {
    CUPS("cups","Imperial","Volume"),
    TEASPOONS("teaspoons","Imperial","volume"),
    TABLESPOONS("tablespoons","Imperial","volume"),
    PINTS("pints", "Metric", "volume"),
    QUARTS("quarts", "Imperial", "volume"),
    GALLONS("gallons", "Imperial", "volume"),
    OUNCES("ounces", "Imperial", "volume"),
    POUNDS("pounds","Imperial","weight"),
    MILLILITERS("milliliters","Metric", "volume"),
    LITERS("liters", "Metric", "volume"),
    GRAMS("grams", "Metric","weight"),
    KILOGRAMS("kilograms", "Metric", "weight"),
    FAHRENHEIT("fahrenheit", "Imperial","temperature"),
    CELSIUS("celsius", "Metric","temperature"),
    UNITS("units","Both","quanity");


    private final String measurement;
    private final String measurementSystem;
    private final String measurementType;

    //Constructor
    MeasurementDetails(String measurement,String measurementSystem, String measurementType){
        this.measurement = measurement;
        this.measurementSystem = measurementSystem;
        this.measurementType = measurementType;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getMeasurementSystem() {
        return measurementSystem;
    }

    public String getMeasurementType() {
        return measurementType;
    }
}
