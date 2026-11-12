package com.smartparking.model;

public enum VehicleType {
    CAR("Car", 30), BIKE("Bike", 15);
    private final String label;
    private final int hourlyRate;
    VehicleType(String label, int hourlyRate) { this.label = label; this.hourlyRate = hourlyRate; }
    public int hourlyRate() { return hourlyRate; }
    @Override public String toString() { return label; }
}
