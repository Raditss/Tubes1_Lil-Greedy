package Services.DataStructure;

public class Pair<T, F> {
    protected T first;
    protected F second;

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

    public void setSecond(F second) {
        this.second = second;
    }

    public F getSecond() {
        return this.second;
    }
}
