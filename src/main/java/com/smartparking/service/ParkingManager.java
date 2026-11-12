package com.smartparking.service;

import com.smartparking.exception.*;
import com.smartparking.model.*;
import com.smartparking.util.CsvFileStore;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/** Owns parking rules and the live collections used by the UI. */
public final class ParkingManager implements Parkable {
    public static final int CAPACITY=20, TYPE_CAPACITY=CAPACITY/2;
    private final Map<String,ParkingSlot> slots=new LinkedHashMap<>();
    private final Map<String,ParkingRecord> activeByVehicle=new HashMap<>();
    private final ArrayList<ParkingRecord> history=new ArrayList<>();
    private final CsvFileStore store;
    private final BillingService billing=new BillingService();
    public ParkingManager(CsvFileStore store){this.store=store; for(int i=1;i<=TYPE_CAPACITY;i++)slots.put("C"+String.format("%02d",i),new ParkingSlot("C"+String.format("%02d",i),VehicleType.CAR)); for(int i=1;i<=TYPE_CAPACITY;i++)slots.put("B"+String.format("%02d",i),new ParkingSlot("B"+String.format("%02d",i),VehicleType.BIKE));}
    public synchronized void load() throws IOException {
        activeByVehicle.clear(); history.clear(); for(ParkingSlot s:slots.values())s.release();
        for(ParkingRecord r:store.loadActive()) { ParkingSlot slot=slots.get(r.getSlotId()); if(slot!=null && slot.getStatus()==ParkingStatus.AVAILABLE && !activeByVehicle.containsKey(r.getRegistrationNumber())) { slot.occupy(r.getTicketId()); activeByVehicle.put(r.getRegistrationNumber(),r); } }
        history.addAll(store.loadHistory());
    }
    @Override public synchronized ParkingRecord parkVehicle(Vehicle vehicle) throws DuplicateVehicleException,InvalidVehicleException,SlotUnavailableException {
        if(vehicle.getRegistrationNumber().isBlank()) throw new InvalidVehicleException("Enter a vehicle registration number.");
        if(!vehicle.getRegistrationNumber().matches("[A-Z0-9 -]{3,15}")) throw new InvalidVehicleException("Use 3–15 letters, numbers, spaces or hyphens for the registration.");
        if(activeByVehicle.containsKey(vehicle.getRegistrationNumber())) throw new DuplicateVehicleException("This vehicle is already parked.");
        ParkingSlot available=null; for(ParkingSlot slot:slots.values()) if(slot.getType()==vehicle.getType() && slot.getStatus()==ParkingStatus.AVAILABLE){available=slot;break;}
        if(available==null)throw new SlotUnavailableException("No " + vehicle.getType().toString().toLowerCase()+" slots are available.");
        ParkingRecord record=new ParkingRecord(new ParkingTicket().getId(),vehicle.getRegistrationNumber(),vehicle.getOwnerName(),vehicle.getType(),available.getId(),LocalDateTime.now());
        available.occupy(record.getTicketId()); activeByVehicle.put(record.getRegistrationNumber(),record); return record;
    }
    public synchronized ParkingRecord parkVehicle(String registrationNumber,VehicleType type,String owner) throws DuplicateVehicleException,InvalidVehicleException,SlotUnavailableException {
        Vehicle vehicle=type==VehicleType.CAR?new Car(registrationNumber,owner):new Bike(registrationNumber,owner); return parkVehicle(vehicle);
    }
    public synchronized ParkingRecord findActive(String key) throws VehicleNotFoundException {
        String normalized=key.trim().toUpperCase(Locale.ROOT); ParkingRecord r=activeByVehicle.get(normalized);
        if(r==null){for(ParkingRecord value:activeByVehicle.values())if(value.getTicketId().equalsIgnoreCase(normalized)){r=value;break;}}
        if(r==null)throw new VehicleNotFoundException("No active vehicle matches that registration or ticket ID."); return r;
    }
    public synchronized long previewCharge(ParkingRecord r,LocalDateTime now){return billing.calculate(r.getVehicleType(),r.getDuration(now));}
    public synchronized ParkingRecord completeExit(String key) throws VehicleNotFoundException {
        ParkingRecord r=findActive(key); LocalDateTime exit=LocalDateTime.now(); r.complete(exit,billing.calculate(r.getVehicleType(),r.getDuration(exit)));
        activeByVehicle.remove(r.getRegistrationNumber()); ParkingSlot slot=slots.get(r.getSlotId()); if(slot!=null)slot.release(); history.add(r); return r;
    }
    public synchronized List<ParkingSlot> getSlots(){return new ArrayList<>(slots.values());}
    public synchronized List<ParkingRecord> getActiveRecords(){return new ArrayList<>(activeByVehicle.values());}
    public synchronized List<ParkingRecord> getHistory(){return new ArrayList<>(history);}
    public synchronized int getOccupiedCount(){return activeByVehicle.size();}
    public synchronized int getOccupiedCount(VehicleType type){int n=0;for(ParkingRecord r:activeByVehicle.values())if(r.getVehicleType()==type)n++;return n;}
    /** Iterator makes history search explicit and safe while building a filtered result. */
    public synchronized List<ParkingRecord> searchHistory(String registration,VehicleType type){
        List<ParkingRecord> result=new ArrayList<>(); String q=registration==null?"":registration.trim().toUpperCase(Locale.ROOT);
        Iterator<ParkingRecord> activeIterator=activeByVehicle.values().iterator();
        while(activeIterator.hasNext()){ParkingRecord r=activeIterator.next(); if(matchesHistoryFilter(r,q,type))result.add(r);}
        Iterator<ParkingRecord> completedIterator=history.iterator();
        while(completedIterator.hasNext()){ParkingRecord r=completedIterator.next(); if(matchesHistoryFilter(r,q,type))result.add(r);}
        result.sort(Comparator.comparing(ParkingRecord::getEntryTime).reversed());
        return result;
    }
    private boolean matchesHistoryFilter(ParkingRecord r,String query,VehicleType type){
        boolean matches=query.isEmpty()||r.getRegistrationNumber().contains(query)||r.getTicketId().toUpperCase(Locale.ROOT).contains(query);
        return matches&&(type==null||r.getVehicleType()==type);
    }
    public synchronized void save() throws IOException {store.save(getActiveRecords(),history);}
}
