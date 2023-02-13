package Services;

import java.util.ArrayList;
import java.util.List;

import Services.DataStructure.Pair;

public class ArrayScore {
    protected List<Double> arrayScore;

    ArrayScore() {
        arrayScore = new ArrayList<Double>();
    }

    ArrayScore(int length) {
        arrayScore = new ArrayList<Double>();
        for (int i = 0; i < length; i++) {
            arrayScore.add(0.0);
        }
    }

    public void setNth(int index, double value) {
        this.arrayScore.set(index, value);
    }

    public void increment(int index, double value) {
        this.arrayScore.set(index, this.arrayScore.get(index) + value);
    }

    public int getLength() {
        return this.arrayScore.size();
    }

    public Pair<Integer, Double> getBestScore() {
        double bestScore = arrayScore.get(0);
        int indexOfBestScore = 0;
        for (int i = 0; i < arrayScore.size(); i++) {
            if (arrayScore.get(i) >= bestScore) {
                indexOfBestScore = i;
                bestScore = arrayScore.get(i);
            }
        }

        Pair<Integer, Double> result = new Pair<Integer, Double>(indexOfBestScore, bestScore);
        return result;
    }

    public void print() {
        System.out.println(arrayScore);
    }
}