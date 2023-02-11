package Services;

import java.util.List;
import Services.DataStructure.Pair;

import Models.GameObject;

public class Direction {

    static public int angleIncrementor(int degree, int value) {
        return ((degree + value) % 360 + 360) % 360;
    }

    static public boolean inTrajectory(int degree, GameObject player, GameObject gameObject) {
        double a = Math.sin(degree);
        double b = -1 * Math.cos(degree);
        double c = Math.cos(degree) * player.getPosition().getY() - Math.sin(degree) * player.getPosition().getX();
        double distance = (a * gameObject.getPosition().getX() + b * gameObject.getPosition().getY() + c)
                / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return distance - gameObject.getSize() <= player.getSize();

    }

    static public Pair<Integer, Double> bestAngle(List<GameObject> gameObjects, GameObject player) {
        DirectionScore directionScore = new DirectionScore();
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            double distanceX = gameObject.getPosition().getX() - player.getPosition().getX();
            double distanceY = gameObject.getPosition().getY() - player.getPosition().getY();
            double rawAngle = Math.atan(distanceY / distanceX);
            int angle = (int) Math.round((rawAngle > 0 ? rawAngle : (2 * Math.PI + rawAngle)) * 360 / (2 * Math.PI));

            // Insert the incrementation value here
            directionScore.increment(angle, 0);

            boolean inTheBackAngle = true;
            int backAngle = angle;
            while (inTheBackAngle) {
                inTheBackAngle = inTrajectory(backAngle, player, gameObject);
                // Insert the incrementation value here
                directionScore.increment(backAngle, 0);
                backAngle = angleIncrementor(backAngle, -1);

            }

            boolean inTheFrontAngle = true;
            int frontAngle = angle;
            while (inTheFrontAngle) {
                // Insert the incrementation value here
                inTheBackAngle = inTrajectory(frontAngle, player, gameObject);
                directionScore.increment(frontAngle, 0);
                frontAngle = angleIncrementor(frontAngle, 1);
            }
        }
        return directionScore.getBestScore();
    }
}
