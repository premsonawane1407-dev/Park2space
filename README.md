# Smart Parking Management System

A beginner friendly Java Swing application for managing a small parking lot. It assigns the first free slot for a car or bike, calculates the exit bill, and keeps active parking and completed transactions in local files.

## Problem statement

Manual parking operations make it difficult to track available spaces, avoid duplicate entries, calculate charges consistently, and retain transaction history. This desktop system provides a simple local workflow for those tasks.

## Features

- Administrator sign in, dashboard, vehicle entry, exit and billing, and parking history screens.
- Fixed capacity of 20 spaces: 10 car spaces (C01–C10) and 10 bike spaces (B01–B10).
- Automatic first available slot allocation, duplicate active vehicle prevention, ticket IDs, and slot detail popups.
- Hourly billing rounded up for every started hour, with a one hour minimum.
- Searchable transaction history and vehicle type filter.
- Active vehicles and completed history saved to `data/active_vehicles.csv` and `data/parking_records.csv`.
- Background startup loading and sequential background saves keep file work off the Swing event thread.

## Technologies

Java 17+, Java Swing, Java Time API, Maven, and local CSV files. The application has no third party runtime dependencies and does not need internet access.

## OOT concepts demonstrated

| Concept | Where to see it |
|---|---|
| Classes, objects, constructors | `Vehicle`, `Car`, `Bike`, `ParkingSlot`, `ParkingRecord`, `ParkingTicket`, `ParkingManager`, `User` and their constructors |
| Method overloading and `static` | `ParkingManager.parkVehicle(Vehicle)` and `parkVehicle(String, VehicleType, String)`; `ParkingManager.CAPACITY`, `VehicleType` rates, and `ParkingTicket.NEXT_ID` |
| Single inheritance | `Car extends Vehicle`, `Bike extends Vehicle` |
| Multilevel inheritance | `ParkingAttendant extends Staff extends User` |
| Abstract class and interfaces | Abstract `Vehicle`; `Billable` implemented by vehicle types; `Parkable` implemented by `ParkingManager` |
| User-defined packages | `model`, `service`, `ui`, `exception`, and `util` under `com.smartparking` |
| Custom exceptions | `InvalidVehicleException`, `DuplicateVehicleException`, `SlotUnavailableException`, `VehicleNotFoundException`, handled by entry and exit screens |
| File handling | `CsvFileStore.loadActive`, `loadHistory`, and `save`; startup restoration in `ParkingManager.load` |
| Multithreading | `DashboardFrame` uses `SwingWorker` to load and a single-thread `ExecutorService` for saves; UI updates return through `SwingUtilities.invokeLater` |
| Collections and iterator | `ArrayList` transaction history and active record snapshots; `HashMap` slot/vehicle lookup; `Iterator` in `ParkingManager.searchHistory` |
| Swing and event handling | `LoginFrame`, `DashboardFrame`; `ActionListener` navigation/forms and `MouseListener` parking cards |

## Project structure

```text
src/main/java/com/smartparking/
├── Main.java
├── exception/    # User-facing domain errors
├── model/        # Vehicles, slots, tickets, transactions, user hierarchy
├── service/      # Parking rules and billing
├── ui/           # Login and dashboard screens
└── util/         # Demo credentials and local CSV storage
data/             # Created automatically when the app saves data
screenshots/      # Place application screenshots here
```

## Run

Install a Java 17 (or newer) JDK and Maven, then from the project directory:

```bash
mvn clean package
java -jar target/smart-parking-management-1.0.0.jar
```

The application creates its `data` folder and CSV files in the current working directory. Run it from the project root so data stays with the repository checkout.

### Demo sign in

- Username: `admin`
- Password: `park123`

This hardcoded login is for classroom demonstration only. It is not production-grade authentication.

### Parking fees

| Vehicle | Rate |
|---|---:|
| Car | ₹30 per started hour |
| Bike | ₹15 per started hour |

The minimum charge is one hour. Partial additional hours are rounded up. For example, a car parked for 2 hours and 15 minutes costs ₹90.

## Screenshots

Capture the running application and save the images in `screenshots/` using these names:

| Screen | File |
|---|---|
| Login | `screenshots/login.png` |
| Main dashboard | `screenshots/dashboard.png` |
| Vehicle entry | `screenshots/vehicle-entry.png` |
| Vehicle exit and bill | `screenshots/vehicle-exit-bill.png` |
| Parking history | `screenshots/parking-history.png` |

## Limitations and future enhancements

- The system supports a single local attendant session and uses demonstration credentials.
- Data files are intended for one running application instance at a time; there is no database or multi-user synchronization.
- Future work could add configurable rates, date filters, printable receipts, and credential management.
