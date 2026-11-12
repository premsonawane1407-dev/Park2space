package com.smartparking.model;

import java.time.Duration;

public interface Billable {
    long calculateCharge(Duration duration);
}
