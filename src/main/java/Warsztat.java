import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Warsztat {
    private static String[][] todoList;
    private static final String FILE_NAME = "tasts.csv";
    private static final String[] OPTIONS = {"add", "remove", "list", "exit"};


    public class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE
    }

    public static void main(String[] args) {
        todoList = addingToTab(FILE_NAME);
        printOptions(OPTIONS);
        todoList = addingToTab("tasts.csv");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    saveTabToFile(FILE_NAME, todoList);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                    addToDoList();
                    break;
                case "remove":
                    removeTask(todoList, getTheNumber());
                    System.out.println("Value was successfully deleted.");
                    break;
                case "list":
                    printTab(todoList);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
        }
    }

    // pobieranie danych z pliku
    public static String[][] addingToTab(String fileName) {
        Path dir = Paths.get(fileName);
        if (!Files.exists(Paths.get(fileName))) {
            System.out.println("File not exist...");
            System.exit(0);
        }
        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];
            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    // Wyświetlanie listy zadań
    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
    }

    //    dodawanie zadań
    private static void addToDoList() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String dueDate = scanner.nextLine();
        System.out.println("Is your task is important: true/false");
        String isImportant = scanner.nextLine();
        todoList = Arrays.copyOf(todoList, todoList.length + 1);
        todoList[todoList.length - 1] = new String[3];
        todoList[todoList.length - 1][0] = description;
        todoList[todoList.length - 1][1] = dueDate;
        todoList[todoList.length - 1][2] = isImportant;
    }

    //    usuwanie zadań
    private static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                todoList = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tab");
        }
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    //    zapisywanie danych i zamykanie programu
    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);

        String[] lines = new String[todoList.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}