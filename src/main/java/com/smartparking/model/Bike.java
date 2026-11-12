package com.smartparking.model;

import java.time.Duration;

public final class Bike extends Vehicle {
    public Bike(String registrationNumber, String ownerName) { super(registrationNumber, ownerName); }
    @Override public VehicleType getType() { return VehicleType.BIKE; }
    @Override public long calculateCharge(Duration duration) { return Car.charge(duration, getType()); }
}
