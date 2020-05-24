import org.apache.commons.validator.GenericValidator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Zarzadzanie_zadaniami {
    static ArrayList<String> toDoList;
    static final String fileName = "tasts.csv";

    public static class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
//        public static final String BLACK = "\033[0;30m";   // BLACK
//        public static final String RED = "\033[0;31m";     // RED
//        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
//        public static final String PURPLE = "\033[0;35m";  // PURPLE
//        public static final String CYAN = "\033[0;36m";    // CYAN
//        public static final String WHITE = "\033[0;37m";   // WHITE
    }

    public static void main(String[] args) {
        toDoList = new ArrayList<>();

        while (true) {
            displayMenu();
        }
    }

    // wyświetlane menu
    private static void displayMenu() {
        Scanner input = new Scanner(System.in);
        System.out.print(ConsoleColors.BLUE + "========================\n"
                + "Task menu: \n"
                + "========================\n" + ConsoleColors.RESET
                + "1. List \n"
                + "2. Add \n"
                + "3. Remove \n"
                + "4. Exit \n"
                + ConsoleColors.BLUE + "========================\n"
                + "\nProszę wybrać opcję: ");
        String selectedMenu = input.nextLine();

        switch (selectedMenu) {
            case "1":
                showToDoList();
                break;
            case "2":
                addToDoList();
                break;
            case "3":
                deleteTodoList();
                break;
            case "4":
                System.exit(0);
            default:
                System.out.println("Chose again from menu!");
                break;
        }
    }


    // pobieranie danych z pliku
    public static void readData(String fileName) {
        toDoList = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner fileReader = new Scanner(file);

            // load file contents into todoLists array
//             toDoList.clear();
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                toDoList.add(data);
//                System.out.println(toDoList);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    //
    private static void showToDoList() {
        readData(fileName);
        if (toDoList.size() > 0) {
            System.out.println("TODO LIST:");
            int index = 0;
            for (String data : toDoList) {
                System.out.println(String.format(ConsoleColors.YELLOW +"%d    %s" + ConsoleColors.RESET, index, data));
                index++;
            }
        } else {
            System.out.println("There is no data!");
        }

    }

    //    dodawanie zadania
    private static void addToDoList() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String dueDate;
        while (true) {
            dueDate = scanner.nextLine();
            if (GenericValidator.isDate(dueDate, "yyyy-MM-dd", true)) {
                break;
            } else {
                System.out.println("Please enter Date in this format YYYY-MM-DD");
            }
        }
        System.out.println("Is your task is important: true/false");
        System.out.print("Chose 't' for true or 'f for false: ");
        String isImportant;
        String reply = scanner.nextLine();
        if (reply.equalsIgnoreCase("t")) {
            isImportant = "true";
        } else if (reply.equalsIgnoreCase("f")) {
            isImportant = "false";
        } else {
            isImportant = "???";
        }

        String newTodoList = description + ", " + dueDate + ", " + isImportant;
        try {
            // write to file
            FileWriter fileWriter = new FileWriter(fileName, true);
            fileWriter.append(String.format("%s %n", newTodoList));
            fileWriter.close();
            System.out.println("Added successfully!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //    usuwanie zadań
    static void deleteTodoList() {
        showToDoList();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select Index: ");
        int index = Integer.parseInt(scanner.nextLine());
        System.out.println(toDoList.get(index));
        try {
            if (index > toDoList.size()) {
                throw new IndexOutOfBoundsException("Entered incorrect data!");
            } else {

                System.out.println(String.format("%d %s", index, toDoList.get(index)));
                System.out.println("Are you sure?");
                System.out.print("Confirm (y/n): ");
                String reply = scanner.nextLine();
                if (reply.equalsIgnoreCase("y")) {
                    toDoList.remove(index);
                    try {
                        FileWriter fileWriter = new FileWriter(fileName, false);

                        // write new data
                        for (String data : toDoList) {
                            fileWriter.append(String.format("%s %n", data));
                        }
                        fileWriter.close();
                        System.out.println("Successfully deleted!");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}