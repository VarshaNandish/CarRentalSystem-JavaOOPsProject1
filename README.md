## 🚗 Car Rental System (Java OOPs Project)

A simple yet functional **console-based Car Rental Management System** built using **Java OOP concepts**, with support for **rental duration tracking**, **price calculation**, and **file-based logging**.


## ✨ Features

- 📅 Track rental and return dates using `LocalDate` and `Period`
- ✅ Rent only available cars
- 💸 Calculate total rental price based on actual days
- 🧾 Logs rental and return activity to `rental_log.txt`
- 📂 Object-Oriented Design with clean separation (Car, Customer, Rental, System)
- 🔁 Menu-driven CLI (Command Line Interface)
- 💾 Data persistence using file handling (log only)


## 🛠 Technologies

- Java 8+  
- `java.time.LocalDate`, `java.time.format.DateTimeFormatter`, `java.time.Period`
- File handling (`BufferedWriter`, `FileWriter`)  
- Collections API (`List`, `ArrayList`)  
- Console input using `Scanner`


## 📁 File Structure

CarRentalSystem/
├── Main.java
├── Car.java
├── Customer.java
├── Rental.java
├── CarRentalSystem.java
├── rental_log.txt ← auto-created after any rental or return


## 🧾 Log Sample (`rental_log.txt`)

RENTED - John Doe rented Toyota Camry on 02-07-2025

RETURNED - John Doe returned Toyota Camry on 05-07-2025 after 3 days

## 🚀 Future Enhancements (Optional Ideas)

GUI using JavaFX or Swing, 

Store data in a file or database (e.g., SQLite), 

Admin panel to manage cars/customers, 

Generate invoices, 

Exception handling for input errors

## 🙋‍♀️ Created By

Varsha S P

Java Enthusiast | Passionate about learning backend & full-stack development

📅 Finalized: July 2025


## 📜 License

This project is open-source and free to use for learning or modification.
