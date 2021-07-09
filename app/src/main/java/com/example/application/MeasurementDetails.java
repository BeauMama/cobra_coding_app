package com.example.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum MeasurementDetails {
    FLUIDOUNCES("fluid ounces","Imperial","volume"),
    TEASPOONS("teaspoons","Imperial","volume"),
    TABLESPOONS("tablespoons","Imperial","volume"),
    CUPS("cups","Imperial","volume"),
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

    UNITS("units","Both","quantity");

    private final String measurement;
    private final String measurementSystem;
    private final String measurementType;

    //Constructor
    MeasurementDetails(String measurement,String measurementSystem, String measurementType){
        this.measurement = measurement;
        this.measurementSystem = measurementSystem;
        this.measurementType = measurementType;
    }

    public static List<String> getMeasurements(String measurementSystem, String measurementType) {
        List<String> measurements = new ArrayList<>();
        for (MeasurementDetails measurement : MeasurementDetails.values()) {

            if (measurement.measurementSystem.toLowerCase().equals(measurementSystem.toLowerCase()) || measurementSystem.toLowerCase().equals("both")) {
                if (measurement.measurementType.toLowerCase().equals(measurementType.toLowerCase())) {
                    measurements.add(measurement.measurement);
                }
            }
        }
        return measurements;
    }

    public static String getMeasurementSystem(String measurement) {
        String measurementSystem = "unknown";
        for (MeasurementDetails measurementDetails : MeasurementDetails.values()) {
            if (measurementDetails.measurement.toLowerCase().equals(measurement.toLowerCase())) {
                measurementSystem = measurementDetails.measurementSystem;
                break;
            }
        }
        return measurementSystem;
    }

    public static String getMeasurementType(String measurement) {
        String measurementType = "unknown";
        for (MeasurementDetails measurementDetails : MeasurementDetails.values()) {
            if (measurementDetails.measurement.toLowerCase().equals(measurement.toLowerCase())) {
                measurementType = measurementDetails.measurementType;
                break;
            }
        }
        return measurementType;
    }
}
