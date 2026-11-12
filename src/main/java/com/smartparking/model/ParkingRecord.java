package com.smartparking.model;

import java.time.Duration;
import java.time.LocalDateTime;

public final class ParkingRecord {
    private final String ticketId, registrationNumber, ownerName, slotId;
    private final VehicleType vehicleType;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long amountPaid;
    public ParkingRecord(String ticketId, String registrationNumber, String ownerName, VehicleType vehicleType, String slotId, LocalDateTime entryTime) {
        this.ticketId=ticketId; this.registrationNumber=registrationNumber; this.ownerName=ownerName; this.vehicleType=vehicleType; this.slotId=slotId; this.entryTime=entryTime;
    }
    public String getTicketId(){return ticketId;} public String getRegistrationNumber(){return registrationNumber;}
    public String getOwnerName(){return ownerName;} public VehicleType getVehicleType(){return vehicleType;}
    public String getSlotId(){return slotId;} public LocalDateTime getEntryTime(){return entryTime;}
    public LocalDateTime getExitTime(){return exitTime;} public long getAmountPaid(){return amountPaid;}
    public boolean isActive(){return exitTime==null;}
    public Duration getDuration(LocalDateTime until){return Duration.between(entryTime, until);}
    public void complete(LocalDateTime exitTime, long amountPaid){this.exitTime=exitTime;this.amountPaid=amountPaid;}
}
