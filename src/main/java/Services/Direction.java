package Services;

import java.util.List;
import Services.DataStructure.Pair;

import Models.GameObject;

public class Direction {

    static public int angleIncrementor(int degree, int value) {
        return ((degree + value) % 360 + 360) % 360;
    }

    static public double distanceToEdge(int degree, GameObject player, double radius) {
        double m = Math.tan(degree);
        double lineB = player.getPosition().getY() - m * player.getPosition().getX();

        double a = Math.pow(m, 2) + 1;
        double b = 2 * m * lineB;
        double c = Math.pow(lineB, 2) - Math.pow(radius, 2);
        double discriminant = Math.pow(b, 2) - 4 * a * c;
        if (discriminant < 0) {
            System.out.println("NaN detected\n");
        }

        double firstPointX = (-b + Math.sqrt(discriminant)) / (2 * a);
        double firstPointY = m * firstPointX + lineB;

        double secondPointX = (-b - Math.sqrt(discriminant)) / (2 * a);
        double secondPointY = m * secondPointX + lineB;

        double playerXDistanceToFirst = player.getPosition().getX() - firstPointX;
        double secondXDistanceToFirst = secondPointX - firstPointX;

        double intersectionDistance = Math
                .sqrt(Math.pow(secondPointY - firstPointY, 2) + Math.pow((secondPointX - firstPointX), 2));
        return intersectionDistance * playerXDistanceToFirst / secondXDistanceToFirst;

    }

    static public boolean inTrajectory(int degree, GameObject player, GameObject gameObject) {
        double radians = Math.toRadians(degree);
        double a = Math.sin(radians);
        double b = -1 * Math.cos(radians);
        double c = Math.cos(radians) * player.getPosition().getY() - Math.sin(radians) * player.getPosition().getX();
        double distance = Math.abs((a * gameObject.getPosition().getX() + b * gameObject.getPosition().getY() + c))
                / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return distance - gameObject.getSize() <= player.getSize();
    }

    static public int bestFoodIndex(List<GameObject> foodList, GameObject player) {
        ArrayScore foodScore = new ArrayScore(foodList.size());
        for (int i = 0; i < foodList.size(); i++) {
            foodScore.increment(i, Config.Movement.directionAffinity(foodList.get(i), player));
        }

        return foodScore.getBestScore().getFirst();

    }

    static public Pair<Integer, Double> bestAngle(List<GameObject> gameObjects, GameObject player, double worldRadius,
            int degreeToAvoid) {
        DirectionScore directionScore = new DirectionScore(360);
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            double distanceX = gameObject.getPosition().getX() - player.getPosition().getX();
            double distanceY = gameObject.getPosition().getY() - player.getPosition().getY();
            double rawAngle = Math.atan2(distanceY, distanceX);
            int angle = (int) angleIncrementor(
                    (int) Math.round((rawAngle > 0 ? rawAngle : (2 * Math.PI + rawAngle)) * 360 / (2 * Math.PI)), 0);

            boolean inAngle = inTrajectory(angle, player, gameObject);

            double score = Config.Movement.directionAffinity(gameObject, player);
            directionScore.increment(angle, inAngle
                    ? score
                    : 0);

            boolean inTheBackAngle = true;
            int backAngle = angleIncrementor(angle, -1);
            do {
                inTheBackAngle = inTrajectory(backAngle, player, gameObject);
                if (inTheBackAngle) {
                    directionScore.increment(backAngle, score);
                }
                backAngle = angleIncrementor(backAngle, -1);
            } while (inTheBackAngle);

            boolean inTheFrontAngle = true;
            int frontAngle = angleIncrementor(angle, -1);
            do {
                inTheFrontAngle = inTrajectory(frontAngle, player, gameObject);
                if (inTheFrontAngle) {
                    directionScore.increment(frontAngle, score);
                }
                frontAngle = angleIncrementor(frontAngle, -1);
            } while (inTheFrontAngle);
        }

        // directionScore.print();
        return directionScore.bestEscape(degreeToAvoid);
    }

    static public Pair<Integer, Double> bestAngle(List<GameObject> gameObjects, GameObject player, double worldRadius) {
        return bestAngle(gameObjects, player, worldRadius, -1);
    }
}
