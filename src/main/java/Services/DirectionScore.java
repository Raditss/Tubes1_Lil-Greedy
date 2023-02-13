package Services;

import java.util.ArrayList;
import java.util.List;

import Services.DataStructure.Interval;
import Services.DataStructure.Pair;

public class DirectionScore extends ArrayScore {
    private List<Interval> intervalList;
    private boolean updatedIntervalList;

    public DirectionScore() {
        super();
        updatedIntervalList = false;
    }

    public DirectionScore(int length) {
        super(length);
        this.intervalList = new ArrayList<Interval>();
        intervalList.add(new Interval(0, length - 1, 360));
        updatedIntervalList = false;
    }

    private void remakeIntervalList() {
        boolean inAnInterval = false;
        this.intervalList = new ArrayList<Interval>();
        for (int i = 0; i < this.arrayScore.size(); i++) {
            if (arrayScore.get(i) >= 0) {
                if (inAnInterval) {
                    intervalList.get(intervalList.size() - 1).incrementUpperBound(1);
                } else {
                    inAnInterval = true;
                    intervalList.add(new Interval(i, i));
                }
            } else {
                inAnInterval = false;
            }
        }
    }

    @Override
    public void increment(int index, double value) {
        super.increment(index, value);
        if (value != 0) {
            updatedIntervalList = false;
        }
    }

    @Override
    public void setNth(int index, double value) {
        if (this.arrayScore.get(index) != value) {
            updatedIntervalList = false;
        }
        super.setNth(index, value);
    }

    public Pair<Integer, Double> bestEscape(Integer degreeToAvoid) {
        if (!updatedIntervalList) {
            remakeIntervalList();
        }

        if (this.intervalList.isEmpty()) {
            return null;
        } else {
            int indexOfWidestInterval = 0;
            Interval widestInterval = intervalList.get(indexOfWidestInterval);
            for (int i = 1; i < intervalList.size(); i++) {
                Interval iteratedInterval = intervalList.get(i);
                if (iteratedInterval.getIntervalLength() >= widestInterval.getIntervalLength()
                        && !iteratedInterval.inTheInterval(degreeToAvoid)) {
                    indexOfWidestInterval = i;
                    widestInterval = iteratedInterval;
                }
            }

            if (widestInterval.inTheInterval(degreeToAvoid)) {
                return null;
            } else {
                int bestAngle = intervalList.get(indexOfWidestInterval).getMiddle();
                return new Pair<Integer, Double>(bestAngle, arrayScore.get(bestAngle));
            }

        }
    }

    public Pair<Integer, Double> bestEscape() {
        return bestEscape(-1);
    }

}
