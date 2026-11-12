package com.smartparking.model;

import java.util.Locale;

/** Shared vehicle state; concrete types supply their own billing behavior. */
public abstract class Vehicle implements Billable {
    private final String registrationNumber;
    private final String ownerName;
    protected Vehicle(String registrationNumber, String ownerName) {
        this.registrationNumber = registrationNumber.trim().toUpperCase(Locale.ROOT);
        this.ownerName = ownerName == null ? "" : ownerName.trim();
    }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getOwnerName() { return ownerName; }
    public abstract VehicleType getType();
}
