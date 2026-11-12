package com.smartparking.util;

import com.smartparking.model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

/** Small local repository. Base64 fields keep commas and quotes safe in our simple CSV files. */
public final class CsvFileStore {
    private static final String ACTIVE_HEADER="ticket,registration,owner,type,slot,entry";
    private static final String HISTORY_HEADER="ticket,registration,owner,type,slot,entry,exit,amount";
    private final Path folder;
    public CsvFileStore(Path folder) { this.folder=folder; }

    public synchronized List<ParkingRecord> loadActive() throws IOException { return read(folder.resolve("active_vehicles.csv"), true); }
    public synchronized List<ParkingRecord> loadHistory() throws IOException { return read(folder.resolve("parking_records.csv"), false); }
    public synchronized void save(List<ParkingRecord> active, List<ParkingRecord> history) throws IOException {
        Files.createDirectories(folder);
        write(folder.resolve("active_vehicles.csv"), ACTIVE_HEADER, active, true);
        write(folder.resolve("parking_records.csv"), HISTORY_HEADER, history, false);
    }
    private List<ParkingRecord> read(Path file, boolean active) throws IOException {
        if (!Files.exists(file)) { Files.createDirectories(folder); Files.writeString(file, active?ACTIVE_HEADER+"\n":HISTORY_HEADER+"\n"); return new ArrayList<>(); }
        List<ParkingRecord> records=new ArrayList<>(); int lineNo=0;
        for (String line:Files.readAllLines(file, StandardCharsets.UTF_8)) {
            lineNo++; if(lineNo==1 || line.isBlank()) continue;
            try {
                String[] c=line.split(",",-1); if(c.length!=(active?6:8)) throw new IllegalArgumentException("wrong column count");
                String id=dec(c[0]), reg=dec(c[1]), owner=dec(c[2]); VehicleType type=VehicleType.valueOf(dec(c[3]));
                ParkingRecord r=new ParkingRecord(id,reg,owner,type,dec(c[4]),LocalDateTime.parse(dec(c[5])));
                if(!active) r.complete(LocalDateTime.parse(dec(c[6])),Long.parseLong(c[7]));
                records.add(r);
            } catch (RuntimeException ex) { System.err.println("Skipping malformed row " + lineNo + " in " + file + ": " + ex.getMessage()); }
        }
        return records;
    }
    private void write(Path path,String header,List<ParkingRecord> records,boolean active) throws IOException {
        List<String> lines=new ArrayList<>(); lines.add(header);
        for(ParkingRecord r:records) {
            String row=enc(r.getTicketId())+","+enc(r.getRegistrationNumber())+","+enc(r.getOwnerName())+","+enc(r.getVehicleType().name())+","+enc(r.getSlotId())+","+enc(r.getEntryTime().toString());
            if(!active) row+=","+enc(r.getExitTime().toString())+","+r.getAmountPaid();
            lines.add(row);
        }
        Files.write(path,lines,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
    }
    private static String enc(String s){return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));}
    private static String dec(String s){return new String(Base64.getUrlDecoder().decode(s),StandardCharsets.UTF_8);}
}
