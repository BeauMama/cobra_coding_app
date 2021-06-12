package com.example.application;

public enum MeasurementDetails {
    CUP("cup","Imperial","Volume"),
    TEASPOON("teaspoon","Imperial","volume"),
    TABLESPOON("tablespoon","Imperial","volume");

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
