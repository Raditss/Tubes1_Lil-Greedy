package Services.DataStructure;

public class Interval extends Pair<Integer, Integer> {
    private int turnaroundPoint = -1;

    public Interval(int lowerBound, int upperBound) {
        super(lowerBound, upperBound);
    }

    public Interval(int lowerBound, int upperBound, int turnaroundPoint) {
        super(lowerBound, upperBound);
        this.turnaroundPoint = turnaroundPoint;
    }

    public int getLowerBound() {
        return super.getFirst();
    }

    public int getUpperBound() {
        return super.getSecond();
    }

    public void incrementLowerBound(int value) {
        this.setLowerBound(this.getLowerBound() + value);
    }

    public void incrementUpperBound(int value) {
        this.setUpperBound(this.getUpperBound() + value);
    }

    public int getTurnaroundPoint() {
        return this.turnaroundPoint;
    }

    public int getIntervalLength() {
        if (this.getTurnaroundPoint() == -1 || this.getUpperBound() >= this.getLowerBound()) {
            return this.getUpperBound() - this.getLowerBound() + 1;
        }

        return this.getTurnaroundPoint() - this.getUpperBound() + this.getLowerBound() + 1;
    }

    public int getMiddle() {
        int middle = this.getLowerBound() + Math.round(this.getIntervalLength() / 2);

        if (this.getTurnaroundPoint() == -1) {
            return middle;
        } else {
            return ((middle % turnaroundPoint)
                    + turnaroundPoint) % turnaroundPoint;
        }
    }

    public void setLowerBound(int lowerBound) {
        super.setFirst(lowerBound);
    }

    public void setUpperBound(int upperBound) {
        super.setSecond(upperBound);
    }

    public boolean inTheMiddle(int value) {
        return this.getLowerBound() < value && this.getUpperBound() > value;
    }

    public boolean inTheEdge(int value) {
        return this.getLowerBound() == value || this.getUpperBound() == value;
    }

    public boolean inTheInterval(int value) {
        return this.inTheEdge(value) || this.inTheMiddle(value);
    }

    public boolean isOneValue() {
        return this.getLowerBound() == this.getUpperBound();
    }

    /**
     * Whether another interval is next to this interval
     * 
     * @param interval
     * @return boolean
     */
    public boolean sideBySide(Interval interval) {
        return rightAfter(interval) || rightBefore(interval);
    }

    public boolean rightBefore(Interval interval) {
        return this.getUpperBound() == interval.getLowerBound();
    }

    public boolean rightAfter(Interval interval) {
        return this.getLowerBound() == interval.getUpperBound();
    }

    /**
     * Split an interval into two and return an interval if the pivot given is not
     * the interval's edge or decrement the edge of the interval if the pivot is the
     * interval's edge.
     * 
     * @param pivot center of the split
     * @return Interval an interval resulting from the split
     */
    public Interval splitByAPivot(int pivot) {
        if (this.isOneValue()) {
            return null;
        }
        if (this.inTheEdge(pivot)) {
            if (pivot == this.getUpperBound()) {
                this.setUpperBound(this.getUpperBound() - 1);
            } else {
                this.setLowerBound(this.getLowerBound() + 1);
            }
            return null;
        } else {
            int oldUpperBound = this.getUpperBound();
            this.setUpperBound(pivot);
            Interval splitInterval = new Interval(pivot + 1, oldUpperBound);
            return splitInterval;
        }
    }

    /**
     * Merge two interval into one
     * 
     * @param interval another interval to be merged into this interval
     * @return boolean whether the merge was valid or not
     */
    public boolean merge(Interval interval) {
        if (!sideBySide(interval)) {
            return false;
        }

        if (rightAfter(interval)) {
            this.setLowerBound(interval.getLowerBound());
        } else {
            this.setUpperBound(interval.getUpperBound());
        }
        return true;
    }
}
