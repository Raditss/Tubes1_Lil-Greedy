package Services;

import Models.GameObject;

public class Config {
    // Ratio of how offensive over how defensive the bot is
    static public double offensiveRatio = 1.1;

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
        static public double minimumDistanceToEdge = 1;
        static public double minimumRatioToEat = 1.1;

        static public class Affinity {
            static public double player = -10.0;
            static public double food = 40.0;
            static public double wormhole = -10.0;
            static public double gasCloud = -1000.0;
            static public double asteroidField = -10.0;
            static public double torpedoSalvo = -5.0;
            static public double superFood = 50.0;
            static public double supernovaPickup = 50.0;
            static public double supernovaBomb = 50.0;
            static public double teleporter = 10.0;
            static public double shield = 10.0;
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
                    if (sizeRatio < minimumRatioToEat) {
                        affinityConstant = -Affinity.player * Math.pow(sizeRatio, 2);
                    } else {
                        affinityConstant = Affinity.player * sizeRatio;
                    }
                    result = affinityConstant;
                    break;
                case ASTEROIDFIELD:
                    affinityConstant = Affinity.asteroidField;
                    result = affinityConstant / distance;
                    break;
                case FOOD:
                    affinityConstant = Affinity.food;
                    result = affinityConstant / distance;
                    break;
                case GASCLOUD:
                    affinityConstant = Affinity.gasCloud;
                    result = affinityConstant / distance;
                    break;
                case SHIELD:
                    affinityConstant = Affinity.shield;
                    result = affinityConstant / distance;
                    break;
                case SUPERFOOD:
                    affinityConstant = Affinity.superFood;
                    result = affinityConstant / distance;
                    break;
                case SUPERNOVABOMB:
                    affinityConstant = Affinity.supernovaBomb;
                    result = affinityConstant / distance;
                    break;
                case SUPERNOVAPICKUP:
                    affinityConstant = Affinity.supernovaPickup;
                    result = affinityConstant / distance;
                    break;
                case TELEPORTER:
                    affinityConstant = Affinity.teleporter;
                    result = affinityConstant / distance;

                    break;
                case TORPEDOSALVO:
                    affinityConstant = Affinity.torpedoSalvo;
                    result = affinityConstant / distance;
                    break;
                case WORMHOLE:
                    affinityConstant = Affinity.wormhole;
                    result = affinityConstant / distance;
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
