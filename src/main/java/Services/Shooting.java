package Services;

import java.util.List;

import Models.GameObject;
import Services.DataStructure.Pair;

public class Shooting {
    static public Pair<Integer, Double> ShootingTarget(List<GameObject> players, GameObject player) {
        ArrayScore targetScore = new ArrayScore(players.size());
        for (int i = 1; i < players.size(); i++) {
            targetScore.increment(i, Config.ShootingPlayer.Salvo.shootingAffinity(player, players.get(i)));
        }
        return targetScore.getBestScore();
    }
}
