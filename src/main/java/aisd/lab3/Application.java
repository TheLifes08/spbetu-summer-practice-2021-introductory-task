package aisd.lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

import aisd.lab3.log.LogFormatter;
import aisd.lab3.structures.BinaryTree;

public class Application {
    private static final Logger logger = Logger.getLogger("aisd.lab3");

    public static void printHelp() {
        System.out.println("List of available options:");
        System.out.println("    -f    Input from file.");
        System.out.println("    -t    Conduct testing.");
        System.out.println("    -s    Enable silent mode.");
        System.out.println("    -h    Print help.");
        System.out.println();
    }

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return now.format(formatter);
    }

    public static void test(String path) {
        int testCount = 0;
        int successTestCount = 0;

        Path filepath = Paths.get(path);

        if (!Files.exists(filepath)) {
            logger.log(Level.WARNING, "Cannot open file: " + path);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filepath)) {
            logger.log(Level.INFO, "File with tests: " + path);

            String line;
            while ((line = reader.readLine()) != null) {
                String result1, result2;
                int separatorIndex1 = line.indexOf('|');
                int separatorIndex2 = line.lastIndexOf('|');

                if (separatorIndex1 != -1 && separatorIndex2 != -1 && separatorIndex1 != separatorIndex2) {
                    String expression = line.substring(0, separatorIndex1);
                    String correctResult1 = line.substring(separatorIndex1 + 1, separatorIndex2);
                    String correctResult2 = line.substring(separatorIndex2 + 1);

                    BinaryTree tree = new BinaryTree();
                    boolean correct = tree.createFromString(expression);

                    if (!correct){
                        result1 = "invalid";
                        result2 = "invalid";
                    } else if (tree.isEmpty()) {
                        result1 = "empty";
                        result2 = "empty";
                    } else {
                        result1 = String.valueOf(tree.getMaximumDepth());
                        result2 = String.valueOf(tree.getInternalPathLength());
                    }

                    testCount++;

                    if (result1.equals(correctResult1) && result2.equals(correctResult2)) {
                        successTestCount++;
                        logger.log(Level.INFO, "\n[Test #" + testCount + " OK]");
                    } else {
                        logger.log(Level.INFO, "\n[Test #" + testCount + " WRONG]");
                    }

                    logger.log(Level.INFO, "Input binary tree: " + expression);
                    logger.log(Level.INFO, "Correct result: Maximum depth = " + correctResult1 + " and internal path length = " + correctResult2);
                    logger.log(Level.INFO, "Test result: Maximum depth = " + result1 + " and internal path length = " + result2);
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot open file: " + e.getMessage());
            return;
        }

        logger.log(Level.INFO, "\nPassed tests: " + successTestCount + "/" + testCount);
    }

    public static void main(String[] args) {
        String expression = null;
        boolean isFromFile = false;
        boolean isTesting = false;
        boolean isSilentMode = false;

        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new LogFormatter());
        logger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("Logs\\" + getCurrentDateTime() + ".log");
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new LogFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot open file: " + e.getMessage());
        }

        if (args.length > 0) {
            for (String arg : args) {
                switch (arg) {
                    case "-f" -> isFromFile = true;
                    case "-t" -> isTesting = true;
                    case "-s" -> isSilentMode = true;
                    case "-h" -> {
                        printHelp();
                        return;
                    }
                    default -> {
                        logger.log(Level.WARNING, "Unknown option: " + arg);
                        return;
                    }
                }
            }
        }

        if (isSilentMode) {
            consoleHandler.setLevel(Level.INFO);
        }

        if (isTesting) {
            test("Tests\\tests.txt");
            return;
        }

        if (isFromFile) {
            logger.log(Level.INFO, "Reading binary tree from file 'input.txt'...");

            Path filepath = Paths.get("input.txt");

            if (!Files.exists(filepath)) {
                logger.log(Level.WARNING, "Cannot open file: input.txt");
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(filepath)) {
                expression = reader.readLine();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Cannot open file: " + e.getMessage());
                return;
            }
        } else {
            System.out.print("[Enter binary tree expression] ");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                expression = reader.readLine();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Cannot open stdin stream: " + e.getMessage());
                return;
            }

            logger.log(Level.INFO, "Entered binary tree expression: " + expression);
        }

        BinaryTree tree = new BinaryTree();
        boolean correct = tree.createFromString(expression);

        if (!correct) {
            logger.log(Level.WARNING, "Invalid binary tree expression.");
            return;
        } else if (tree.isEmpty()) {
            logger.log(Level.WARNING, "Binary tree is empty.");
            return;
        }

        logger.log(Level.INFO, "Created binary tree: " + tree.getString() + "\n");

        int maximumDepth = tree.getMaximumDepth();
        int internalPathLength = tree.getInternalPathLength();

        logger.log(Level.INFO, "Binary tree maximum depth: " + maximumDepth);
        logger.log(Level.INFO, "Binary tree internal path length: " + internalPathLength);
    }
}
