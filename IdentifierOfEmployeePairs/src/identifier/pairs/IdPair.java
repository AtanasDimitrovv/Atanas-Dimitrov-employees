package identifier.pairs;

import java.util.Objects;

public class IdPair {
    private int id1;
    private int id2;

    public IdPair(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdPair that)) {
            return false;
        }
        return id1 == that.id1 && id2 == that.id2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }

    @Override
    public String toString() {
        return "EmployeeIdPair{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                '}';
    }
}
