A console-based logistics management system for managing delivery packages, transport vehicles, and delivery routes across major Australian cities.

## 📦 Project Overview

This application is intended for use by logistics employees at a large Australian company entering the freight industry. It supports operations like registering packages, creating and updating delivery routes, assigning trucks, and tracking deliveries.

## 🛠️ Features (to be implemented)

- Create and manage delivery packages
- Create and manage delivery routes
- Assign trucks and packages to routes
- View current package and route states
- Save/load application state
- Search for suitable delivery routes
- Estimate delivery times

## 🗂️ Package Structure

com.company.logistics
├── commands # Command pattern implementations for each user operation
├── core # Application engine and StartUp class
├── exceptions # Custom exception classes
├── models # Domain models: DeliveryPackage, DeliveryRoute, Truck, etc.
└── utils # Helper classes: input validation, constants, etc.

## 🔧 Speed Models

We offer three interchangeable speed‐model implementations behind a common `SpeedService` interface, wired at startup in the `EngineFactory`.

| Model         | Description                                                                                                           |
|---------------|-----------------------------------------------------------------------------------------------------------------------|
| **Constant**    | Flat average speed across the year (e.g. 80 km/h).                                                                  |
| **Seasonal**    | Month-based multipliers:<br/>• Dec, Jan, Feb → ×1.10<br/>• Jun, Jul, Aug → ×0.85<br/>• Others → ×1.00                |
| **Sinusoidal**  | Smooth annual variation defined by:<br/><code>v(d) = v_base + A * sin(2π * (d – φ) / 365)</code><br/>Peak on Jan 26. |

where `d` = day of year, `A` = amplitude (e.g. ±10 km/h), and `φ` shifts the peak to January 26. |

![Speed Models Over a Year](docs/speed_models.png)

> *Figure: Constant (black), Seasonal (blue), and Sinusoidal (green) speed profiles over a 365-day year.*

---

## 💡 Example Console Session

```text
# 1) Create a multi-stop route (SYD → MEL → ADL)
CREATEROUTE SYD,MEL,ADL 2025-07-01T08:00
→ Route was created with ID 1.

# 2) List routes (with ETAs)
LISTROUTEINFO
→ Route 1:
    1. SYD at 2025-07-01T08:00
    2. MEL at 2025-07-01T18:49
    3. ADL at 2025-07-02T03:59

# 3) Search for sub-segments
FINDROUTE SYD ADL
→ (same as above)

FINDROUTE MEL ADL
→ (same as above)

FINDROUTE ADL SYD
→ No routes found between ADL and SYD.

# 4) Create packages
CREATEPACKAGE Alice_Smith_0412345678 20 SYD ADL
→ Package was created with ID 2.

CREATEPACKAGE Bob_Jones_0498765432 15 MEL ADL
→ Package was created with ID 3.

# 5) View all packages
LISTPACKAGEINFO
→ Package 2:
     From SYD → ADL, 20.00 kg, Contact: Alice_Smith_0412345678

   Package 3:
     From MEL → ADL, 15.00 kg, Contact: Bob_Jones_0498765432

# 6) Assign package to route
ASSIGNPACKAGETOROUTE 2 1
→ Assigned Package 2 to Route 1.

# 7) View updated package info
LISTPACKAGEINFO
→ Package 2:
     From SYD → ADL, 20.00 kg, Contact: Alice_Smith_0412345678
     Expected Arrival: 2025-07-02T03:59

# 8) Invalid assignment example
CREATEPACKAGE Charlie 5 PER SYD
→ Package was created with ID 4.
ASSIGNPACKAGETOROUTE 4 1
→ City PER not on route

# 9) Exit
EXIT

## 🧪 Testing

The `tests/` directory is reserved for JUnit 5 tests covering core functionality

## 🚀 How to Run

1. Open the project in IntelliJ IDEA.
2. Run the `StartUp.java` class located in `com.company.logistics` to launch the application.

## 📌 Use Case Highlights

- Register and assign packages to existing routes.
- Bulk assign packages based on location and truck capacity.
- Get delivery estimates based on distances and time.
- Track packages.

## 📂 Save/Load