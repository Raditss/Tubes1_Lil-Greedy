package Services.DataStructure;

public class Pair<T, F> {
    private T first;
    private F second;

    public Pair(T first, F second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getFirst() {
        return this.first;
    }

    public void setSsecond(F second) {
        this.second = second;
    }

    public F getSsecond() {
        return this.second;
    }
}