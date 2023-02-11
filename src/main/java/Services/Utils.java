package Services;

import Models.GameObject;

public class Utils {
    static public double getDistanceBetween(GameObject object1, GameObject object2) {
        double triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        double triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    static public int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }
}
