Great! Let's walk through a **fully object-oriented design** of the popular **Parking Lot System**, a real-world OOP design question commonly asked in senior roles like **Emirates Group IT**.

---

## ✅ 2. **Design Parking Lot System (OOP Focus)**

### 🎯 **Goal**:

Design a parking lot that supports:

- Multiple levels/floors
- Different vehicle types: Car, Truck, Bike
- Different slot sizes: Small, Medium, Large
- Park and un-park vehicles
- Track available slots
- Extendable design (scalable OOP)

---

### 🧠 Key OOP Concepts:

- Inheritance
- Polymorphism
- Composition
- Abstraction
- SOLID principles (e.g., Open/Closed, SRP)

---

## ✅ Step-by-Step Design

### 📦 1. **Enum for VehicleType and SlotType**

```java
public enum VehicleType {
    BIKE, CAR, TRUCK;
}

public enum SlotType {
    SMALL, MEDIUM, LARGE;
}
```

---

### 🚗 2. **Vehicle Hierarchy**

```java
public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public VehicleType getType() {
        return type;
    }
}

public class Car extends Vehicle {
    public Car(String plate) {
        super(plate, VehicleType.CAR);
    }
}

public class Bike extends Vehicle {
    public Bike(String plate) {
        super(plate, VehicleType.BIKE);
    }
}

public class Truck extends Vehicle {
    public Truck(String plate) {
        super(plate, VehicleType.TRUCK);
    }
}
```

---

### 🅿️ 3. **ParkingSlot Class**

```java
public class ParkingSlot {
    private final int slotId;
    private final SlotType slotType;
    private boolean isOccupied;
    private Vehicle parkedVehicle;

    public ParkingSlot(int id, SlotType type) {
        this.slotId = id;
        this.slotType = type;
        this.isOccupied = false;
    }

    public boolean canFit(Vehicle vehicle) {
        return !isOccupied && fitsType(vehicle.getType());
    }

    private boolean fitsType(VehicleType vehicleType) {
        switch (slotType) {
            case SMALL:
                return vehicleType == VehicleType.BIKE;
            case MEDIUM:
                return vehicleType == VehicleType.CAR || vehicleType == VehicleType.BIKE;
            case LARGE:
                return true;
            default:
                return false;
        }
    }

    public void park(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.isOccupied = true;
    }

    public void unpark() {
        this.parkedVehicle = null;
        this.isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public int getSlotId() {
        return slotId;
    }
}
```

---

### 🧱 4. **ParkingFloor Class**

```java
import java.util.*;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public ParkingFloor(int number, List<ParkingSlot> slots) {
        this.floorNumber = number;
        this.slots = slots;
    }

    public Optional<ParkingSlot> getAvailableSlot(Vehicle vehicle) {
        return slots.stream()
                .filter(slot -> slot.canFit(vehicle))
                .findFirst();
    }

    public void printStatus() {
        long occupied = slots.stream().filter(ParkingSlot::isOccupied).count();
        System.out.println("Floor " + floorNumber + " Occupied: " + occupied + "/" + slots.size());
    }
}
```

---

### 🅿️ 5. **ParkingLot Manager Class**

```java
public class ParkingLot {
    private final List<ParkingFloor> floors;

    public ParkingLot(List<ParkingFloor> floors) {
        this.floors = floors;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            Optional<ParkingSlot> slot = floor.getAvailableSlot(vehicle);
            if (slot.isPresent()) {
                slot.get().park(vehicle);
                System.out.println("✅ Vehicle parked at floor " + floor + ", slot " + slot.get().getSlotId());
                return true;
            }
        }
        System.out.println("❌ No slot available for vehicle type: " + vehicle.getType());
        return false;
    }

    public void showStatus() {
        floors.forEach(ParkingFloor::printStatus);
    }
}
```

---

### 🧪 6. **Test it like a real system**

```java
public class ParkingTest {
    public static void main(String[] args) {
        // Create slots
        List<ParkingSlot> floor1Slots = new ArrayList<>();
        floor1Slots.add(new ParkingSlot(1, SlotType.SMALL));
        floor1Slots.add(new ParkingSlot(2, SlotType.MEDIUM));
        floor1Slots.add(new ParkingSlot(3, SlotType.LARGE));

        List<ParkingFloor> floors = Arrays.asList(
                new ParkingFloor(0, floor1Slots)
        );

        // Initialize Parking Lot
        ParkingLot lot = new ParkingLot(floors);

        // Park vehicles
        lot.parkVehicle(new Bike("BIKE-1"));
        lot.parkVehicle(new Car("CAR-1"));
        lot.parkVehicle(new Truck("TRUCK-1")); // May fail if no large slot

        lot.showStatus();
    }
}
```

---

## ✅ OOP Design Summary

| Class          | Responsibility                             |
| -------------- | ------------------------------------------ |
| `Vehicle`      | Abstract parent of `Bike`, `Car`, etc.     |
| `ParkingSlot`  | Holds one vehicle of allowed type          |
| `ParkingFloor` | Manages all slots in a floor               |
| `ParkingLot`   | Top-level manager to park/unpark           |
| `Enums`        | Standardized types (VehicleType, SlotType) |

---

## 🚀 Extensible Features for Production

You can extend this system with:

- `UnparkVehicle()`
- Pricing system (add `entryTime`, `exitTime`)
- Multi-level floors
- Integration with Spring Boot REST APIs
- Add Redis to manage real-time status across instances

---

Would you like:

- 💻 Elevator System OOP Design next?
- 🌐 Spring Boot REST APIs for this system?
- 🧪 Unit tests with JUnit 5?

Let me know!
