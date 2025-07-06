import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.io.BufferedWriter; // For writing to files
import java.io.FileWriter;
import java.io.IOException;

// Car class with availability and price calculation
class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true; // Initially available
    }

    public String getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }
    public void rent() { isAvailable = false; }
    public void returnCar() { isAvailable = true; }

    // Custom method to calculate rental price
    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }
}

// Customer class for customer details
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
}

// Rental class using LocalDate for tracking start and return dates
class Rental {
    private Car car;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate returnDate; // Nullable until returned

    public Rental(Car car, Customer customer, LocalDate startDate) {
        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
    }

    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    // Calculate days between start and return
    public int getRentalDays() {
        if (returnDate != null && !returnDate.isBefore(startDate)) {
            return Period.between(startDate, returnDate).getDays();
        }
        return 0;
    }
}

// Main car rental system class
class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) { cars.add(car); }
    public void addCustomer(Customer customer) { customers.add(customer); }

    // Helper method to write log entries to file
    private void logToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Failed to log to file: " + e.getMessage());
        }
    }

    // Rent car with LocalDate
    public void rentCar(Car car, Customer customer, LocalDate startDate) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, startDate));
            // Log rental info to file
            logToFile("rental_log.txt", "RENTED - " + customer.getName() + " rented " + car.getBrand() + " " + car.getModel() + " on " + startDate.format(formatter));
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    // Return car and calculate price using dates
    public void returnCar(Car car, LocalDate returnDate) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentalToRemove.setReturnDate(returnDate);
            int days = rentalToRemove.getRentalDays();
            double total = car.calculatePrice(days);
            rentals.remove(rentalToRemove);

            System.out.println("\n== Rental Return Summary ==\n");
            System.out.println("Customer Name: " + rentalToRemove.getCustomer().getName());
            System.out.println("Car: " + car.getBrand() + " " + car.getModel());
            System.out.println("Start Date: " + rentalToRemove.getStartDate());
            System.out.println("Return Date: " + returnDate);
            System.out.println("Total Rental Days: " + days);
            System.out.printf("Total Price: $%.2f\n", total);

            // Log return info to file
            logToFile("rental_log.txt", "RETURNED - " + rentalToRemove.getCustomer().getName() + " returned " + car.getBrand() + " " + car.getModel() + " on " + returnDate.format(formatter) + " after " + days + " days");
        } else {
            System.out.println("Car was not rented.");
        }
    }

    // Menu with restored Y/N confirmation
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Varsha's Car Rental System =====");
            System.out.println("\nHow can we help you?");
            System.out.println("\n1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();
                System.out.print("Enter start date (dd-MM-yyyy): ");
                String start = scanner.nextLine();
                LocalDate startDate = LocalDate.parse(start, formatter);

                System.out.println("\nAvailable Cars:");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();
                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                    addCustomer(newCustomer);

                    System.out.println("\n== Rental Summary ==");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Start Date: " + startDate);
                    System.out.println("Price per day: $" + selectedCar.calculatePrice(1));

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, startDate);
                        System.out.println("\nCar rented successfully.");
                        System.out.println("Happy and safe journey!\n");
                    } else {
                        System.out.println("\nRental canceled.\n");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.\n");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();
                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }
                if (carToReturn != null) {
                    System.out.print("Enter return date (dd-MM-yyyy): ");
                    String returnStr = scanner.nextLine();
                    LocalDate returnDate = LocalDate.parse(returnStr, formatter);
                    returnCar(carToReturn, returnDate);
                    System.out.println("\nCar returned successfully!");
                    System.out.println("Please visit us again!\n");
                } else {
                    System.out.println("Invalid car ID or car is not rented.\n");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        System.out.println("\nSee You again!");
    }
}

// Main class to run the system
public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();
        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
        rentalSystem.menu();
    }
}
