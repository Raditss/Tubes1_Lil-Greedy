package Services;

import Models.GameObject;

public class Config {
    static public class Movement {
        static public class Affinity {
            static public double player = -10.0;
            static public double food = 10.0;
            static public double wormhole = 10.0;
            static public double gasCloud = -10.0;
            static public double asteroidField = -10.0;
            static public double torpedoSalvo = -5.0;
            static public double superFood = 50.0;
            static public double supernovaPickup = 50.0;
            static public double supernovaBomb = 50.0;
            static public double teleporter = 10.0;
            static public double shield = 10.0;
        }

        static double distanceFunction(GameObject gameObject, GameObject player) {
            double distance = Math.sqrt(Math.pow((gameObject.getPosition().getX() - player.getPosition().getX()), 2)
                    + Math.pow(gameObject.getPosition().getY() - player.getPosition().getY(), 2));
            double result;
            double affinityConstant;
            switch (gameObject.getGameObjectType()) {
                case PLAYER:
                    affinityConstant = Affinity.player;
                    break;
                case ASTEROIDFIELD:
                    affinityConstant = Affinity.asteroidField;
                    break;
                case FOOD:
                    affinityConstant = Affinity.food;
                    break;
                case GASCLOUD:
                    affinityConstant = Affinity.gasCloud;
                    break;
                case SHIELD:
                    affinityConstant = Affinity.shield;
                    break;
                case SUPERFOOD:
                    affinityConstant = Affinity.superFood;
                    break;
                case SUPERNOVABOMB:
                    affinityConstant = Affinity.supernovaBomb;
                    break;
                case SUPERNOVAPICKUP:
                    affinityConstant = Affinity.supernovaPickup;
                    break;
                case TELEPORTER:
                    affinityConstant = Affinity.teleporter;
                    break;
                case TORPEDOSALVO:
                    affinityConstant = Affinity.torpedoSalvo;
                    break;
                case WORMHOLE:
                    affinityConstant = Affinity.wormhole;
                    break;
                default:
                    affinityConstant = Affinity.food;
                    break;
            }

            result = affinityConstant / Math.pow(distance, 2);
            return result;
        }
    }
}
