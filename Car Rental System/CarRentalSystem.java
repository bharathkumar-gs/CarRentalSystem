package com.compozent_internship;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CarRentalSystem {
	public static void main(String[] args) {
		RentalSystem rentalSystem = new RentalSystem();
		rentalSystem.run();
	}
}

class Car {
	private String make;
	private String model;
	private int year;
	private boolean available;
	private double rentalPrice;

	public Car(String make, String model, int year, double rentalPrice) {
		this.make = make;
		this.model = model;
		this.year = year;
		this.rentalPrice = rentalPrice;
		this.available = true;
	}

	private String rentedBy;

	public String getRentedBy() {
		return rentedBy;
	}

	public void rent(String rentedBy) {
		available = false;
		this.rentedBy = rentedBy;
	}

	public void returnCar() {
		available = true;
		this.rentedBy = null;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public int getYear() {
		return year;
	}

	public boolean isAvailable() {
		return available;
	}

	public void rent() {
		available = false;
	}

	public double getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(double rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

	@Override
	public String toString() {
		return year + " " + make + " " + model;
	}
}

class Customer {
	private String name;
	private String password;
	private double accountBalance;
	private List<String> rentalHistory;

	public Customer(String name, String password) {
		this.name = name;
		this.password = password;
		this.accountBalance = 0.0;
		this.rentalHistory = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void addToAccountBalance(double amount) {
		accountBalance += amount;
	}

	public boolean deductFromAccountBalance(double amount) {
		if (amount <= accountBalance) {
			accountBalance -= amount;
			return true;
		}
		return false;
	}

	public List<String> getRentalHistory() {
		return rentalHistory;
	}

	public void addToRentalHistory(String carInfo, double rentalPrice, String rentalTime) {
		rentalHistory.add("Car: " + carInfo + ", Price: $" + rentalPrice + ", Time: " + rentalTime);
	}

	@Override
	public String toString() {
		return name;
	}
}

class Admin {
	private String username;
	private String password;

	public Admin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}

class RentalSystem {
	private List<Car> cars;
	private List<Customer> customers;
	private Admin admin;
	private Scanner scanner;
	private boolean isAdminLoggedIn;
	private boolean isUserLoggedIn;
	private Customer loggedInUser;
	private double adminBalance;

	public RentalSystem() {
		cars = new ArrayList<>();
		customers = new ArrayList<>();
		admin = new Admin("admin", "admin123");
		scanner = new Scanner(System.in);
		isAdminLoggedIn = false;
		isUserLoggedIn = false;
		loggedInUser = null;
		adminBalance = 0.0;

		// Initialize some cars
		cars.add(new Car("Toyota", "Camry", 2022, 30.0));
		cars.add(new Car("Honda", "Accord", 2021, 35.0));
		cars.add(new Car("Ford", "Mustang", 2023, 50.0));
		cars.add(new Car("Nissan", "GT-R", 2023, 120.0));
		cars.add(new Car("Mercedes", "Benz", 2022, 99.0));
	}

	public void run() {
		while (true) {
			if (isAdminLoggedIn) {
				displayAdminMenu();
			} else if (isUserLoggedIn) {
				displayUserMenu();
			} else {
				displayFirstPage();
			}
		}
	}

	private void displayFirstPage() {
		System.out.println("\nWelcome to the Car Rental System!");
		System.out.println("1. Create Account");
		System.out.println("2. Log In as Admin");
		System.out.println("3. Log In as User");
		System.out.println("0. Exit");

		int choice = getIntInput("Enter your choice: ");

		switch (choice) {
		case 1:
			createAccount();
			break;
		case 2:
			adminLogin();
			break;
		case 3:
			userLogin();
			break;
		case 0:
			System.out.println("Exiting Car Rental System. Goodbye!");
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice. Please try again.");
		}
	}

	private void displayAdminMenu() {
		System.out.println("\nAdmin Menu");
		System.out.println("1. View Available Cars");
		System.out.println("2. View Rented Cars");
		System.out.println("3. Add Car");
		System.out.println("4. Delete Car");
		System.out.println("5. Set Car Rental Price");
		System.out.println("6. View User Account Balances");
		System.out.println("7. Admin rental history ");
		System.out.println("8. User  rental history ");
		System.out.println("9. Log Out");

		int choice = getIntInput("Enter your choice: ");

		switch (choice) {
		case 1:
			displayAvailableCars();
			break;
		case 2:
			displayRentedCars();
			break;
		case 3:
			addCar();
			break;
		case 4:
			deleteCar();
			break;
		case 5:
			setCarRentalPrice();
			break;
		case 6:
			viewAdminAccountBalance();
			break;
		case 9:
			isAdminLoggedIn = false;
			break;
		case 7:
			viewAdminRentalHistory();
			break;

		case 8:
			viewAllUsersRentalHistory();
			break;
		default:
			System.out.println("Invalid choice. Please try again.");
		}
	}

	private void viewAdminAccountBalance() {
		System.out.println("Admin's Account Balance: $" + adminBalance);
	}

	private Customer findCustomerByCar(Car car) {
		for (Customer customer : customers) {
			if (customer.getRentalHistory().stream().anyMatch(history -> history.contains(car.toString()))) {
				return customer;
			}
		}
		return null;
	}

	private void displayUserMenu() {
		System.out.println("\nUser Menu");
		System.out.println("1. Rent a Car");
		System.out.println("2. Return a Car");
		System.out.println("3. View Rental History");
		System.out.println("4. View Account Balance");
		System.out.println("5. Add Money to Account");
		System.out.println("6. Log Out");

		int choice = getIntInput("Enter your choice: ");

		switch (choice) {
		case 1:
			rentCar();
			break;
		case 2:
			returnCar();
			break;
		case 3:
			viewRentalHistory();
			break;
		case 4:
			viewAccountBalance();
			break;
		case 5:
			addMoneyToAccount();
			break;
		case 6:
			isUserLoggedIn = false;
			loggedInUser = null;
			break;
		default:
			System.out.println("Invalid choice. Please try again.");
		}
	}

	private void rentCar() {
		System.out.println("Available Cars:");
		displayAvailableCars();

		List<Car> availableCars = cars.stream().filter(Car::isAvailable).collect(Collectors.toList());

		if (!availableCars.isEmpty()) {
			int carIndex = getIntInput("\nEnter the number of the car you want to rent: ") - 1;

			if (carIndex >= 0 && carIndex < availableCars.size()) {
				Car selectedCar = availableCars.get(carIndex);
				if (selectedCar.isAvailable()) {
					double rentalPrice = selectedCar.getRentalPrice();
					if (loggedInUser.getAccountBalance() >= rentalPrice) {
						// Ask user to pay before renting
						double paymentAmount = getDoubleInput(
								"Payment required: $" + rentalPrice + "\nEnter the amount to pay: ");
						if (paymentAmount >= rentalPrice) {
							loggedInUser.deductFromAccountBalance(rentalPrice);
							adminBalance += rentalPrice;
							selectedCar.rent();
							String rentalTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
							loggedInUser.addToRentalHistory(selectedCar.toString(), rentalPrice, rentalTime);
							System.out.println("Rent successful. Enjoy your ride! Price: $" + rentalPrice);
						} else {
							System.out.println("Insufficient payment. Please pay the required amount to rent a car.");
						}
					} else {
						System.out.println("Insufficient funds. Please add money to your account.");
					}
				} else {
					System.out.println("Sorry, the selected car is not available for rent.");
				}
			} else {
				System.out.println("Invalid car selection. Please try again.");
			}
		} else {
			System.out.println("No cars available for rent at the moment. Please try again later.");
		}
	}

	private void displayRentedCars() {
		List<Car> rentedCars = cars.stream().filter(car -> !car.isAvailable()).collect(Collectors.toList());

		if (!rentedCars.isEmpty()) {
			System.out.println("\nRented Cars:");
			int count = 1;
			for (Car car : rentedCars) {
				Customer rentedBy = findCustomerByCar(car);
				String rentalTime = findRentalTime(car, rentedBy);
				System.out.println(
						count + ". " + car + ", Rented By: " + rentedBy.getName() + ", Rental Time: " + rentalTime);
				count++;
			}
		} else {
			System.out.println("\nNo cars are currently rented.");
		}
	}

	private String findRentalTime(Car car, Customer customer) {
		for (String rentalInfo : customer.getRentalHistory()) {
			if (rentalInfo.contains(car.toString())) {
				int startIndex = rentalInfo.indexOf("Time: ") + 6;
				int endIndex = rentalInfo.length();
				return rentalInfo.substring(startIndex, endIndex);
			}
		}
		return "N/A";
	}

	private void viewAccountBalance() {
		System.out.println("Account Balance: $" + loggedInUser.getAccountBalance());
	}

	private int getIntInput(String prompt) {
		System.out.print(prompt);
		while (!scanner.hasNextInt()) {
			System.out.println("Invalid input. Please enter a valid integer.");
			scanner.next();
		}
		return scanner.nextInt();
	}

	private void createAccount() {
		String customerName = getStringInput("Enter your name: ");
		String password = getStringInput("Enter your password: ");
		Customer customer = findOrCreateCustomer(customerName, password);
		System.out.println("Account created successfully for " + customer.getName() + "!");
	}

	private void adminLogin() {
		String username = getStringInput("Enter admin username: ");
		String password = getStringInput("Enter admin password: ");

		if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
			isAdminLoggedIn = true;
		} else {
			System.out.println("Invalid admin credentials. Please try again.");
		}
	}

	private void userLogin() {
		String customerName = getStringInput("Enter your name: ");
		String password = getStringInput("Enter your password: ");
		Customer customer = findCustomerByName(customerName);

		if (customer != null && customer.getPassword().equals(password)) {
			loggedInUser = customer;
			isUserLoggedIn = true;
		} else {
			System.out.println("Invalid customer credentials. Please check the name and password and try again.");
		}
	}

	private void addMoneyToAccount() {
		double amount = getDoubleInput("Enter the amount to add to your account: ");
		loggedInUser.addToAccountBalance(amount);
		System.out.println("Amount added successfully. New account balance: $" + loggedInUser.getAccountBalance());
	}

	private String getStringInput(String prompt) {
		System.out.print(prompt);
		return scanner.next();
	}

	private void displayAvailableCars() {
		int count = 1;
		for (Car car : cars) {
			if (car.isAvailable()) {
				System.out.println(count + ". " + car + ", Rental Price: $" + car.getRentalPrice());
				count++;
			}
		}
	}

	private void addCar() {
		String make = getStringInput("Enter car make: ");
		String model = getStringInput("Enter car model: ");
		int year = getIntInput("Enter car year: ");
		double rentalPrice = getDoubleInput("Enter car rental price: ");

		Car newCar = new Car(make, model, year, rentalPrice);
		cars.add(newCar);

		System.out.println("Car added successfully: " + newCar);
	}

	private void deleteCar() {
		System.out.println("Available Cars:");
		displayAvailableCars();

		int carIndex = getIntInput("Enter the number of the car you want to delete: ") - 1;

		if (carIndex >= 0 && carIndex < cars.size()) {
			Car deletedCar = cars.remove(carIndex);
			System.out.println("Car deleted successfully: " + deletedCar);
		} else {
			System.out.println("Invalid car selection. Please try again.");
		}
	}

	private void setCarRentalPrice() {
		System.out.println("Available Cars:");
		displayAvailableCars();

		int carIndex = getIntInput("Enter the number of the car you want to set rental price for: ") - 1;

		if (carIndex >= 0 && carIndex < cars.size()) {
			Car selectedCar = cars.get(carIndex);
			double newRentalPrice = getDoubleInput("Enter the new rental price for " + selectedCar + ": ");
			selectedCar.setRentalPrice(newRentalPrice);
			System.out.println("Rental price set successfully. New rental price: $" + newRentalPrice);
		} else {
			System.out.println("Invalid car selection. Please try again.");
		}
	}

	private void viewUserAccountBalances() {
		for (Customer customer : customers) {
			System.out.println(customer.getName() + "'s Account Balance: $" + customer.getAccountBalance());
		}
		System.out.println("Admin's Account Balance: $" + adminBalance);
	}

	private double getDoubleInput(String prompt) {
		System.out.print(prompt);
		while (!scanner.hasNextDouble()) {
			System.out.println("Invalid input. Please enter a valid number.");
			scanner.next(); // consume the invalid input
		}
		return scanner.nextDouble();
	}

	private Customer findOrCreateCustomer(String name, String password) {
		Customer customer = findCustomerByName(name);
		if (customer == null) {
			customer = new Customer(name, password);
			customers.add(customer);
		}
		return customer;
	}

	private Customer findCustomerByName(String name) {
		for (Customer customer : customers) {
			if (customer.getName().equalsIgnoreCase(name)) {
				return customer;
			}
		}
		return null;
	}

	private void viewRentalHistory() {
		if (loggedInUser != null) {
			List<String> rentalHistory = loggedInUser.getRentalHistory();

			if (!rentalHistory.isEmpty()) {
				System.out.println(loggedInUser.getName() + "'s Rental History:");
				for (String rentalInfo : rentalHistory) {
					System.out.println(rentalInfo);
				}
			} else {
				System.out.println("No rental history found for " + loggedInUser.getName());
			}
		} else {
			System.out.println("No user logged in. Please log in first.");
		}
	}

	private void returnCar() {
		if (loggedInUser != null) {
			System.out.println("Cars currently rented:");
			displayRentedCars();

			int carIndex = getIntInput("\nEnter the number of the car you want to return: ") - 1;

			if (carIndex >= 0 && carIndex < cars.size()) {
				Car selectedCar = cars.get(carIndex);
				if (!selectedCar.isAvailable() && selectedCar.getRentedBy() != null
						&& selectedCar.getRentedBy().equals(loggedInUser.getName())) {
					selectedCar.returnCar();
					String rentalTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					double rentalPrice = selectedCar.getRentalPrice();
					loggedInUser.addToRentalHistory(selectedCar.toString() + " (Returned)", rentalPrice, rentalTime);
					// Apply cashback of 10%
					double cashback = rentalPrice * 0.1;
					loggedInUser.addToAccountBalance(cashback);
					System.out.println("Return successful. Thank you for using our service, " + loggedInUser.getName()
							+ "! Cashback applied: $" + cashback);
				} else {
					System.out.println("You can only return cars that you rented. Please select a car you rented.");
				}
			} else {
				System.out.println("Invalid car selection. Please try again.");
			}
		} else {
			System.out.println("No user logged in. Please log in first.");
		}
	}

	private void viewAdminRentalHistory() {
		System.out.println("\nAdmin's Rental History:");
		for (Customer customer : customers) {
			List<String> rentalHistory = customer.getRentalHistory();
			if (!rentalHistory.isEmpty()) {
				System.out.println(customer.getName() + "'s Rental History:");
				for (String rentalInfo : rentalHistory) {
					System.out.println(rentalInfo);
				}
			}
		}
	}

	private void viewAllUsersRentalHistory() {
		System.out.println("\nAll Users' Rental History:");
		for (Customer customer : customers) {
			List<String> rentalHistory = customer.getRentalHistory();
			if (!rentalHistory.isEmpty()) {
				System.out.println(customer.getName() + "'s Rental History:");
				for (String rentalInfo : rentalHistory) {
					System.out.println(rentalInfo);
				}
			}
		}
	}

}
