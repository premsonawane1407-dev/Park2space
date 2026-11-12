package com.smartparking.model;

import java.time.Duration;

public final class Car extends Vehicle {
    public Car(String registrationNumber, String ownerName) { super(registrationNumber, ownerName); }
    @Override public VehicleType getType() { return VehicleType.CAR; }
    @Override public long calculateCharge(Duration duration) { return charge(duration, getType()); }
    static long charge(Duration duration, VehicleType type) {
        long hours = duration.toHours();
        if (!duration.minusHours(hours).isZero()) hours++;
        hours = Math.max(1, hours);
        return hours * type.hourlyRate();
    }
}
