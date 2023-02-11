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
        double a = Math.sin(degree);
        double b = -1 * Math.cos(degree);
        double c = Math.cos(degree) * player.getPosition().getY() - Math.sin(degree) * player.getPosition().getX();
        double distance = (a * gameObject.getPosition().getX() + b * gameObject.getPosition().getY() + c)
                / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return distance - gameObject.getSize() <= player.getSize();
    }

    static public Pair<Integer, Double> bestAngle(List<GameObject> gameObjects, GameObject player, double worldRadius) {
        ArrayScore directionScore = new ArrayScore(360);
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            double distanceX = gameObject.getPosition().getX() - player.getPosition().getX();
            double distanceY = gameObject.getPosition().getY() - player.getPosition().getY();
            double rawAngle = Math.atan(distanceY / distanceX);
            int angle = (int) Math.round((rawAngle > 0 ? rawAngle : (2 * Math.PI + rawAngle)) * 360 / (2 * Math.PI));

            directionScore.increment(angle, Config.Movement.directionAffinity(gameObject, player));

            boolean inTheBackAngle = true;
            int backAngle = angle - 1;
            while (inTheBackAngle) {
                inTheBackAngle = inTrajectory(backAngle, player, gameObject);
                if (inTheBackAngle) {
                    directionScore.increment(backAngle, Config.Movement.directionAffinity(gameObject, player));
                }
                backAngle = angleIncrementor(backAngle, -1);
            }

            boolean inTheFrontAngle = true;
            int frontAngle = angle + 1;
            while (inTheFrontAngle) {
                // Insert the incrementation value here
                inTheFrontAngle = inTrajectory(frontAngle, player, gameObject);
                if (inTheFrontAngle) {
                    directionScore.increment(backAngle, Config.Movement.directionAffinity(gameObject, player));
                }
                frontAngle = angleIncrementor(frontAngle, 1);
            }
        }

        for (int i = 0; i < 360; i++) {
            if (distanceToEdge(i, player, worldRadius) <= Config.Movement.minimumDistanceToEdge + player.getSize()) {
                directionScore.setNth(i, 0);
            }
        }

        return directionScore.getBestScore();
    }
}
