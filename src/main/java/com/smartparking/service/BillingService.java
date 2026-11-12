package com.smartparking.service;

import com.smartparking.model.*;
import java.time.Duration;

public final class BillingService {
    public long calculate(Vehicle vehicle, Duration duration) { return vehicle.calculateCharge(duration); }
    public long calculate(VehicleType type, Duration duration) {
        long hours=duration.toHours();
        if(!duration.minusHours(hours).isZero())hours++;
        hours=Math.max(1,hours);
        return hours*type.hourlyRate();
    }
}
