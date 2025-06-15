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