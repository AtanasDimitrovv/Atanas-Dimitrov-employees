package identifier.employee;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    private final int empId;
    private final int projectId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public Employee(int empId, int projectId, LocalDate dateFrom, LocalDate dateTo) {
        this.empId = empId;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public int getEmpId() {
        return empId;
    }

    public int getProjectId() {
        return projectId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee employee)) {
            return false;
        }
        return empId == employee.empId
                && projectId == employee.projectId
                && Objects.equals(dateFrom, employee.dateFrom)
                && Objects.equals(dateTo, employee.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, projectId, dateFrom, dateTo);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", projectId=" + projectId +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}
