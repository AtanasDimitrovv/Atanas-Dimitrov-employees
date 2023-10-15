package identifier.ui;

import identifier.EmployeePairIdentifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSelector {
    private Set<String> files;

    private final static String PATH_TO_DIR = ".";
    private final static String FILE_EXTENSION = "csv";

    public FileSelector() {
        files = new HashSet<>();
    }

    public Object[][] getPairData() {
        populateFiles();

        EmployeePairIdentifier identifier = new EmployeePairIdentifier();

        Scanner scanner = new Scanner(System.in);
        boolean rightFileName = false;
        String fileName;
        Object[][] data = new Object[0][];

        while (!rightFileName) {
            System.out.println("Select one of the given files:");
            for (String s : files) {
                System.out.println("  -  " + s);
            }

            System.out.println("Enter file name: ");
            fileName = scanner.nextLine();

            try {
                data = identifier.getCommonProjectsOfPair(fileName);
            } catch (IllegalStateException e) {
                System.out.println("Please enter valid name");
                continue;
            }

            rightFileName = true;
        }

        if (data == null) {
            Object[][] result = {};
            return result;
        } else {
            return data;
        }
    }

    private void populateFiles() {
        try (Stream<Path> stream = Files.list(Paths.get(PATH_TO_DIR))) {
            files = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(file -> file.endsWith(FILE_EXTENSION))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
