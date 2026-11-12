package com.smartparking.model;

public final class ParkingSlot {
    private final String id;
    private final VehicleType type;
    private ParkingStatus status = ParkingStatus.AVAILABLE;
    private String activeTicketId;
    public ParkingSlot(String id, VehicleType type) { this.id = id; this.type = type; }
    public String getId() { return id; }
    public VehicleType getType() { return type; }
    public ParkingStatus getStatus() { return status; }
    public String getActiveTicketId() { return activeTicketId; }
    public void occupy(String ticketId) { status = ParkingStatus.OCCUPIED; activeTicketId = ticketId; }
    public void release() { status = ParkingStatus.AVAILABLE; activeTicketId = null; }
}
