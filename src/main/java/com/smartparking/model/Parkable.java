package com.smartparking.model;

import com.smartparking.exception.DuplicateVehicleException;
import com.smartparking.exception.InvalidVehicleException;
import com.smartparking.exception.SlotUnavailableException;

public interface Parkable {
    ParkingRecord parkVehicle(Vehicle vehicle) throws DuplicateVehicleException, InvalidVehicleException, SlotUnavailableException;
}
