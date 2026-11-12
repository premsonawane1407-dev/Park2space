package com.smartparking.model;

import java.util.concurrent.atomic.AtomicLong;

public final class ParkingTicket {
    private static final AtomicLong NEXT_ID = new AtomicLong(System.currentTimeMillis() % 1_000_000);
    private final String id;
    public ParkingTicket() { id = "T" + NEXT_ID.incrementAndGet(); }
    public ParkingTicket(String id) { this.id = id; }
    public String getId() { return id; }
}
