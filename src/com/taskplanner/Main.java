package com.taskplanner;

import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;

public class Main {
    private static TaskManager taskManager = new TaskManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Personalized Task Planner!");
        
        while (true) {
            printMenu();
            int choice = -1;
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
                continue;
            }

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewAllTasks();
                    break;
                case 3:
                    updateTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    System.out.println("Exiting application. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Task Planner Menu ---");
        System.out.println("1. Add New Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Delete Task");
        System.out.println("5. Exit");
    }

    private static void addTask() {
        System.out.println("\n--- Add New Task ---");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter Deadline (e.g., YYYY-MM-DD): ");
        String deadline = scanner.nextLine();

        Task.Priority priority = null;
        while (priority == null) {
            System.out.print("Enter Priority (HIGH, MEDIUM, LOW): ");
            try {
                priority = Task.Priority.valueOf(scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Please enter HIGH, MEDIUM, or LOW.");
            }
        }

        taskManager.addTask(title, description, deadline, priority, Task.Status.PENDING);
    }

    private static void viewAllTasks() {
        System.out.println("\n--- All Tasks ---");
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static void updateTask() {
        System.out.println("\n--- Update Task ---");
        System.out.print("Enter Task ID to update: ");
        int id = -1;
        try {
            id = scanner.nextInt();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid ID format.");
            scanner.nextLine();
            return;
        }

        Task task = taskManager.getTaskById(id);
        if (task == null) {
            System.out.println("Task not found with ID: " + id);
            return;
        }

        System.out.println("Updating Task: " + task.getTitle());
        System.out.println("1. Update Details (Title, Desc, Deadline, Priority)");
        System.out.println("2. Mark as Completed/Pending");
        System.out.print("Enter choice: ");
        
        int choice = -1;
        try {
             choice = scanner.nextInt();
             scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            return;
        }

        if (choice == 1) {
            System.out.print("Enter New Title (leave blank to keep current): ");
            String title = scanner.nextLine();
            
            System.out.print("Enter New Description (leave blank to keep current): ");
            String desc = scanner.nextLine();
            
            System.out.print("Enter New Deadline (leave blank to keep current): ");
            String deadline = scanner.nextLine();
            
            Task.Priority priority = null;
            System.out.print("Enter New Priority (HIGH, MEDIUM, LOW) (leave blank to keep current): ");
            String priorityInput = scanner.nextLine();
            if (!priorityInput.isEmpty()) {
                 try {
                    priority = Task.Priority.valueOf(priorityInput.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid priority. Keeping current priority.");
                }
            }
            
            if (taskManager.updateTaskWithoutStatus(id, title, desc, deadline, priority)) {
                System.out.println("Task details updated successfully.");
            } else {
                System.out.println("Failed to update task.");
            }

        } else if (choice == 2) {
             Task.Status status = null;
             while (status == null) {
                System.out.print("Enter Status (PENDING, COMPLETED): ");
                try {
                    status = Task.Status.valueOf(scanner.nextLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status.");
                }
            }
            if (taskManager.updateTaskStatus(id, status)) {
                System.out.println("Task status updated successfully.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void deleteTask() {
        System.out.println("\n--- Delete Task ---");
        System.out.print("Enter Task ID to delete: ");
        int id = -1;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid ID format.");
            scanner.nextLine();
            return;
        }

        if (taskManager.deleteTask(id)) {
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Task not found with ID: " + id);
        }
    }
}
