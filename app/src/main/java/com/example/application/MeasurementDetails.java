package com.example.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum MeasurementDetails {
    SELECT("select", "select", "select"),
    CELSIUS("C", "Metric","temperature"),
    FAHRENHEIT("F", "Imperial","temperature"),
    CUPS("cups","Imperial","volume"),
    FLUIDOUNCES("fluid ounces","Imperial","volume"),
    GALLONS("gallons", "Imperial", "volume"),
    GRAMS("grams", "Metric","weight"),
    KILOGRAMS("kilograms", "Metric", "weight"),
    LITERS("liters", "Metric", "volume"),
    MILLILITERS("milliliters","Metric", "volume"),
    OUNCES("ounces", "Imperial", "weight"),
    PINTS("pints", "Imperial", "volume"),
    POUNDS("pounds","Imperial","weight"),
    QUARTS("quarts", "Imperial", "volume"),
    TABLESPOONS("tablespoons","Imperial","volume"),
    TEASPOONS("teaspoons","Imperial","volume"),
    UNITS("units","Units","quantity");

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
            if (measurement.measurement.toLowerCase().equals("select") && !measurementType.toLowerCase().equals("quantity")) {
                measurements.add(measurement.measurement);
            } else if (measurement.measurementSystem.toLowerCase().equals(measurementSystem.toLowerCase()) ||
                    measurement.measurementSystem.toLowerCase().equals("units") ||
                    measurementSystem.toLowerCase().equals("all")) {
                if (measurement.measurementType.toLowerCase().equals(measurementType.toLowerCase()) ||
                        measurementType.toLowerCase().equals("all")) {
                    if(!measurement.measurementType.toLowerCase().equals("temperature")) {
                        measurements.add(measurement.measurement);
                    }
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
