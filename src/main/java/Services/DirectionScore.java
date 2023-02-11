package Services;

import java.util.ArrayList;
import java.util.List;

import Services.DataStructure.Pair;

public class DirectionScore {
    private List<Double> directionScore;

    DirectionScore() {
        directionScore = new ArrayList<Double>(360);
        for (int i = 0; i < 360; i++) {
            directionScore.set(i, 0.0);
        }
    }

    public void increment(int index, double value) {
        this.directionScore.set(index, this.directionScore.get(index) + value);
    }

    public Pair<Integer, Double> getBestScore() {
        double bestScore = 0;
        int indexOfBestScore = 0;
        for (int i = 0; i < directionScore.size(); i++) {
            if (directionScore.get(i) > bestScore) {
                indexOfBestScore = i;
                bestScore = directionScore.get(i);
            }
        }

        Pair<Integer, Double> result = new Pair<Integer, Double>(indexOfBestScore, bestScore);
        return result;
    }
}