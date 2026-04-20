import java.io.*;
import java.util.*;

// Employee2 Class
class Employee2 implements Serializable {
    int id;
    String name;
    double salary;

    Employee2(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    void display() {
        System.out.println("ID: " + id + ", Name: " + name + ", Salary: " + salary);
    }
}

// Manager Class
class EmployeeManager {
    ArrayList<Employee2> list = new ArrayList<>();
    final String fileName = "employees.dat";

    void addEmployee(Employee2 e) {
        list.add(e);
        saveToFile();
    }

    void viewEmployees() {
        loadFromFile();
        if (list.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        for (Employee2 e : list) {
            e.display();
        }
    }

    void searchEmployee(int id) {
        loadFromFile();
        for (Employee2 e : list) {
            if (e.id == id) {
                e.display();
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    void saveToFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(list);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving file.");
        }
    }

    void loadFromFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            list = (ArrayList<Employee2>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            list = new ArrayList<>();
        }
    }
}

// Main Class
public class EmployeeManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EmployeeManager manager = new EmployeeManager();

        while (true) {
            System.out.println("\n1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();

                    manager.addEmployee(new Employee2(id, name, salary));
                    System.out.println("Employee Added!");
                    break;

                case 2:
                    manager.viewEmployees();
                    break;

                case 3:
                    System.out.print("Enter ID to search: ");
                    int searchId = sc.nextInt();
                    manager.searchEmployee(searchId);
                    break;

                case 4:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}