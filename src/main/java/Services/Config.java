package Services;

import Models.GameObject;

public class Config {
    // Ratio of how offensive over how defensive the bot is
    static public double offensiveRatio = 1.1;
    // The radius in which hostile objects will be detected
    static public double safeRadius = 200;
    // The radius in which food are considered to be eaten
    static public double foodRadius = 200;

    static public class ShootingPlayer {
        static public class Salvo {
            static public double degreeFieldOfViewThreshold = 120;
            static public double shootingSizeThreshold = 50;

            static public double shootingAffinity(GameObject player, GameObject enemy) {
                double distance = Utils.getDistanceBetween(player, enemy);
                double degreeFieldOfView = 2 * Math.atan2(enemy.getSize() / 2, distance);
                if (degreeFieldOfView < degreeFieldOfViewThreshold) {
                    return 0;
                } else {
                    return Math.log(degreeFieldOfView) * 100 / Math.log(360);
                }
            }
        }
    }

    static public class Movement {
        static public double maximumDistanceToEdge = 10;
        // The lowest ratio between enemy's size and our size for them to be
        // considered hostile
        // Below this ratio they aren't hostile
        static public double hostilePlayerSizeRatio = 1;

        static public class Affinity {
            static public double biggerPlayer = -10.0;
            static public double smallerPlayer = 100.0;
            static public double food = 40.0;
            static public double wormhole = -1.0;
            static public double gasCloud = -10.0;
            static public double asteroidField = -10.0;
            static public double torpedoSalvo = -5.0;
            static public double superFood = 50.0;
            static public double supernovaPickup = 50.0;
            static public double supernovaBomb = 50.0;
            static public double teleporter = 10.0;
            static public double shield = 10.0;
        }

        static public boolean isHostile(GameObject item, GameObject us) {
            boolean result;
            switch (item.getGameObjectType()) {
                case GASCLOUD:
                    result = true;
                    break;
                case SUPERNOVABOMB:
                    result = true;
                    break;
                case TORPEDOSALVO:
                    result = true;
                    break;
                case ASTEROIDFIELD:
                    result = true;
                    break;
                case WORMHOLE:
                    result = true;
                    break;
                case PLAYER:
                    result = (item.getSize() / us.getSize()) >= hostilePlayerSizeRatio;
                    break;
                default:
                    result = false;
                    break;
            }
            return result;
        }

        static public boolean isFood(GameObject item, GameObject us) {
            boolean result;
            switch (item.getGameObjectType()) {
                case FOOD:
                    result = true;
                    break;
                case SUPERFOOD:
                    result = true;
                    break;
                case PLAYER:
                    result = (item.getSize() / us.getSize()) < hostilePlayerSizeRatio;
                    break;
                default:
                    result = false;
                    break;
            }
            return result;
        }

        static double directionAffinity(GameObject gameObject, GameObject player) {
            double distance = Math.sqrt(Math.pow((gameObject.getPosition().getX() - player.getPosition().getX()), 2)
                    + Math.pow(gameObject.getPosition().getY() - player.getPosition().getY(), 2));
            double result;
            double affinityConstant;
            switch (gameObject.getGameObjectType()) {
                case PLAYER:
                    // Size ratio between the player and the bot
                    double sizeRatio = gameObject.getSize() / player.getSize();
                    if (sizeRatio >= hostilePlayerSizeRatio) {
                        result = Affinity.biggerPlayer;
                    } else {
                        result = Affinity.smallerPlayer / sizeRatio / Math.sqrt(distance);
                    }
                    break;
                case ASTEROIDFIELD:
                    affinityConstant = Affinity.asteroidField;
                    result = affinityConstant;
                    break;
                case FOOD:
                    result = Affinity.food / Math.sqrt(distance);
                    break;
                case GASCLOUD:
                    affinityConstant = Affinity.gasCloud;
                    result = affinityConstant;
                    break;
                case SHIELD:
                    affinityConstant = Affinity.shield;
                    result = affinityConstant;
                    break;
                case SUPERFOOD:
                    result = Affinity.superFood / Math.sqrt(distance);
                    break;
                case SUPERNOVABOMB:
                    affinityConstant = Affinity.supernovaBomb;
                    result = affinityConstant;
                    break;
                case SUPERNOVAPICKUP:
                    affinityConstant = Affinity.supernovaPickup;
                    result = affinityConstant;
                    break;
                case TELEPORTER:
                    affinityConstant = Affinity.teleporter;
                    result = affinityConstant;
                    break;
                case TORPEDOSALVO:
                    affinityConstant = Affinity.torpedoSalvo;
                    result = affinityConstant;
                    break;
                case WORMHOLE:
                    affinityConstant = Affinity.wormhole;
                    result = affinityConstant;
                    break;
                default:
                    affinityConstant = Affinity.food;
                    result = affinityConstant / distance;
                    break;
            }

            return result;
        }
    }
}
