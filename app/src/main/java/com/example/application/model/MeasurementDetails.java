package com.example.application.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum class to handle set measurement, measurementSystem, and measurementType for the recipe.
 */
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

    /**
     * Constructor
     *
     * @param measurement The name of the measurement.
     * @param measurementSystem The system for the measurement.
     * @param measurementType The measurement type like volume, weight, temperature.
     * @return the enum.
     */
    MeasurementDetails(String measurement, String measurementSystem, String measurementType){
        this.measurement = measurement;
        this.measurementSystem = measurementSystem;
        this.measurementType = measurementType;
    }

    /**
     * Gets all the related measurements for a specific system and type.
     *
     * @param measurementSystem The measurement system to get measurements for. Use all to get
     *                          measurements for all systems.
     * @param measurementType The measurement type to get measurements for. Use all to get
     *                        measurements for all types.
     * @return A list of measurements.
     */
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

    /**
     * Gets the type of measurement.
     *
     * @param measurement The measurement to get the type for.
     * @return The type of the measurement.
     */
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
