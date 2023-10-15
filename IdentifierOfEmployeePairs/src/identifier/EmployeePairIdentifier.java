package identifier;

import identifier.employee.Employee;
import identifier.pairs.IdPair;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collections;


public class EmployeePairIdentifier {
    private final List<Employee> employeeList;
    private final IdPair bestPair;

    private final DateTimeFormatter dateTimeFormatter;

    private final static String EXCEPTION_FILE = "exceptions.txt";
    private final static String SEPARATOR = ",";

    private static final int EMP_ID_INDEX = 0;
    private static final int PROJECT_ID_INDEX = 1;
    private static final int DATE_FROM_INDEX = 2;
    private static final int DATE_TO_INDEX = 3;

    public EmployeePairIdentifier() {
        employeeList = new ArrayList<>();
        bestPair = new IdPair(-1, -1);
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("[yyyy-MM-dd]" + "[yyyy/MM/dd]" + "[MM/dd/yyyy]"
                        + "[MM-dd-yyyy]" + "[yyyyMMdd]" + "[MM.dd.yyyy]" + "[yyyy.MM.dd]"));
        dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
    }

    public Object[][] getCommonProjectsOfPair(String fileName) {
        setupEmployees(fileName);
        findBestPair();

        if (bestPair.getId1() == -1 || bestPair.getId2() == -2) {
            return null;
        }

        List<Object[]> data = new ArrayList<>();
        int listSize = employeeList.size();
        for (int i = 0; i < listSize; i++) {
            for (int j = i + 1; j < listSize; j++) {
                Employee first = employeeList.get(i);
                Employee second = employeeList.get(j);

                if (first.getProjectId() == second.getProjectId()) {
                    int less = Math.min(first.getEmpId(), second.getEmpId());
                    int more = Math.max(first.getEmpId(), second.getEmpId());

                    if (less == bestPair.getId1() && more == bestPair.getId2()) {
                        LocalDate biggerBegin = first.getDateFrom().isAfter(second.getDateFrom()) ?
                                first.getDateFrom() : second.getDateFrom();
                        LocalDate lesserEnd = first.getDateTo().isAfter(second.getDateTo()) ?
                                second.getDateTo() : first.getDateTo();
                        long days = ChronoUnit.DAYS.between(biggerBegin, lesserEnd);
                        if (days > 0) {
                            Object[] dataRow = {less, more, first.getProjectId(), days};
                            data.add(dataRow);
                        }
                    }
                }
            }
        }

        return data.toArray(new Object[0][]);
    }

    private void logException(String message, Exception e) {
        try (var writer = new FileWriter(EXCEPTION_FILE, true)) {
            e.printStackTrace(new PrintWriter(writer));
            writer.write(message + System.lineSeparator());
            writer.flush();

        } catch (IOException ex) {
            System.out.println("Could not write to file");
        }
    }

    private void setupEmployees(String fileName) {
        Path filePath = Path.of(fileName);

        try (BufferedReader bufferedReader = Files.newBufferedReader(filePath)) {
            String line;
            int row = 1;

            while ((line = bufferedReader.readLine()) != null) {
                String[] employeeInfo = line.split(SEPARATOR);
                if (employeeInfo[DATE_TO_INDEX].equals("NULL")) {
                    try {
                        employeeList.add(new Employee(Integer.parseInt(employeeInfo[EMP_ID_INDEX]),
                                Integer.parseInt(employeeInfo[PROJECT_ID_INDEX]),
                                LocalDate.parse(employeeInfo[DATE_FROM_INDEX], dateTimeFormatter),
                                LocalDate.now()));
                    } catch (NumberFormatException | DateTimeParseException e) {
                        logException("Row " + row + " is in wrong format", e);
                    }
                } else {
                    try {
                        employeeList.add(new Employee(Integer.parseInt(employeeInfo[EMP_ID_INDEX]),
                                Integer.parseInt(employeeInfo[PROJECT_ID_INDEX]),
                                LocalDate.parse(employeeInfo[DATE_FROM_INDEX], dateTimeFormatter),
                                LocalDate.parse(employeeInfo[DATE_TO_INDEX], dateTimeFormatter)));
                    } catch (NumberFormatException | DateTimeParseException e) {
                        logException("Row " + row + " is in wrong format", e);
                    }
                }

                row++;
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    private void findBestPair() {
        Map<IdPair, Long> idPairMap = new HashMap<>();

        int listSize = employeeList.size();
        for (int i = 0; i < listSize; i++) {
            for (int j = i + 1; j < listSize; j++) {
                Employee first = employeeList.get(i);
                Employee second = employeeList.get(j);

                if (first.getProjectId() == second.getProjectId()) {
                    LocalDate biggerBegin = first.getDateFrom().isAfter(second.getDateFrom()) ?
                            first.getDateFrom() : second.getDateFrom();
                    LocalDate lesserEnd = first.getDateTo().isAfter(second.getDateTo()) ?
                            second.getDateTo() : first.getDateTo();
                    long days = ChronoUnit.DAYS.between(biggerBegin, lesserEnd);

                    if (days > 0) {
                        IdPair idPair = new IdPair(first.getEmpId(), second.getEmpId());
                        if (idPairMap.containsKey(idPair)) {
                            idPairMap.put(idPair, idPairMap.get(idPair) + days);
                        } else {
                            idPairMap.put(idPair, days);
                        }
                    }
                }
            }
        }

        modifyPair(idPairMap);
    }

    private void modifyPair(Map<IdPair, Long> idPairMap) {
        Map<Long, IdPair> sortedByDays = new TreeMap<>(Collections.reverseOrder());

        for (Map.Entry<IdPair, Long> e : idPairMap.entrySet()) {
            sortedByDays.put(e.getValue(), e.getKey());
        }

        for (Map.Entry<Long, IdPair> e : sortedByDays.entrySet()) {
            bestPair.setId1(e.getValue().getId1());
            bestPair.setId2(e.getValue().getId2());
            break;
        }
    }
}
