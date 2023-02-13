package Services;

import Enums.*;
import Models.*;
import Services.DataStructure.Pair;

import java.util.*;
import java.util.stream.*;

public class BotService {
        private GameObject bot;
        private PlayerAction playerAction;
        private GameState gameState;

        public BotService() {
                this.playerAction = new PlayerAction();
                this.gameState = new GameState();
        }

        public GameObject getBot() {
                return this.bot;
        }

        public void setBot(GameObject bot) {
                this.bot = bot;
        }

        public PlayerAction getPlayerAction() {
                return this.playerAction;
        }

        public void setPlayerAction(PlayerAction playerAction) {
                this.playerAction = playerAction;
        }

        public void computeNextPlayerAction(PlayerAction playerAction) {
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = new Random().nextInt(360);
                System.out.println("Bot is ticking\n");

                if (!gameState.getGameObjects().isEmpty()) {
                        List<GameObject> playerList = gameState.getGameObjects()
                                        .stream().filter(item -> item.getGameObjectType() == ObjectTypes.PLAYER)
                                        .sorted(Comparator
                                                        .comparing(item -> Utils.getDistanceBetween(bot, item)))
                                        .collect(Collectors.toList());

                        List<GameObject> hostileObjectList = gameState.getGameObjects()
                                        .stream().filter(item -> Config.Movement.isHostile(item, this.bot))
                                        .sorted(Comparator
                                                        .comparing(item -> Utils.getDistanceBetween(bot, item)))
                                        .collect(Collectors.toList());
                        List<GameObject> foodList = gameState.getGameObjects()
                                        .stream()
                                        .filter(item -> Config.Movement.isFood(item, this.bot))
                                        .sorted(Comparator
                                                        .comparing(item -> Utils.getDistanceBetween(bot, item)))
                                        .collect(Collectors.toList());
                        // var foodList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var playerList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() == ObjectTypes.PLAYER)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var wormholeList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() == ObjectTypes.WORMHOLE)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var gasCloudList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var asteroidFieldList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() ==
                        // ObjectTypes.ASTEROIDFIELD)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var superFoodList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var supernovaPickupList = gameState.getGameObjects()
                        // .stream()
                        // .filter(item -> item.getGameObjectType() == ObjectTypes.SUPERNOVAPICKUP)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var supernovaBombList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() ==
                        // ObjectTypes.SUPERNOVABOMB)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var teleporterList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() == ObjectTypes.TELEPORTER)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var shieldList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SHIELD)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // var torpedoSalvoList = gameState.getGameObjects()
                        // .stream().filter(item -> item.getGameObjectType() ==
                        // ObjectTypes.TORPEDOSALVO)
                        // .sorted(Comparator
                        // .comparing(item -> getDistanceBetween(bot, item)))
                        // .collect(Collectors.toList());

                        // Pair<Integer, Double> bestTarget = Shooting.ShootingTarget(playerList, bot);

                        double distanceToMid = getDistanceBetween(new Position(0, 0));
                        double distanceToEdge = gameState.getWorld().getRadius() - distanceToMid - this.bot.getSize();
                        boolean hostileNearby = false;
                        boolean edgeNearby = distanceToEdge < Config.Movement.maximumDistanceToEdge;
                        int i = 0;

                        int headingToCenter = getHeadingBetween(new Position(0, 0));
                        int headingToEdge = (((180 + headingToCenter) % 360) + 360) % 360;

                        while (i < hostileObjectList.size() && !hostileNearby) {
                                GameObject object = hostileObjectList.get(i);
                                hostileNearby = Utils.getDistanceBetween(object,
                                                this.bot) - object.getSize() <= Config.safeRadius;
                                i++;
                        }

                        if (hostileNearby) {
                                System.out.println("Hostile nearby");
                                List<GameObject> hostileNearbyList = hostileObjectList.stream()
                                                .filter(item -> Utils.getDistanceBetween(item,
                                                                this.bot) - item.getSize() < Config.safeRadius)
                                                .collect(Collectors.toList());
                                System.out.println(hostileNearbyList.size());
                                Integer degreeDirection = edgeNearby ? Direction.bestAngle(
                                                hostileNearbyList,
                                                this.bot,
                                                gameState.getWorld().getRadius(), headingToEdge).getFirst()
                                                : Direction.bestAngle(
                                                                hostileNearbyList,
                                                                this.bot,
                                                                gameState.getWorld().getRadius()).getFirst();
                                if (degreeDirection == null) {
                                        playerAction.setAction(PlayerActions.ACTIVATESHIELD);
                                } else {
                                        playerAction.setHeading(degreeDirection);
                                        playerAction.setAction(PlayerActions.FORWARD);

                                }
                                System.out.println(degreeDirection);
                        } else if (edgeNearby) {
                                playerAction.setHeading(headingToCenter);
                                playerAction.setAction(PlayerActions.FORWARD);
                        } else {
                                System.out.println("Food nearby");
                                List<GameObject> foodNearbyList = foodList.stream()
                                                .filter(item -> Utils.getDistanceBetween(item,
                                                                this.bot) - item.getSize() < Config.foodRadius)
                                                .collect(Collectors.toList());
                                System.out.println(foodNearbyList.size());
                                GameObject foodToGet = foodNearbyList
                                                .get(Direction.bestFoodIndex(foodNearbyList, this.bot));
                                System.out.println(foodToGet.getGameObjectType());
                                playerAction.setHeading(getHeadingBetween(foodToGet));
                                playerAction.setAction(PlayerActions.FORWARD);
                        }

                }

                this.playerAction = playerAction;
        }

        public GameState getGameState() {
                return this.gameState;
        }

        public void setGameState(GameState gameState) {
                this.gameState = gameState;
                updateSelfState();
        }

        private void updateSelfState() {
                Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream()
                                .filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
                optionalBot.ifPresent(bot -> this.bot = bot);
        }

        private int getHeadingBetween(GameObject otherObject) {
                int direction = (int) (long) Math
                                .round(Utils.toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                                                otherObject.getPosition().x - bot.getPosition().x)));
                return (direction + 360) % 360;
        }

        private int getHeadingBetween(Position otherPos) {
                int direction = (int) (long) Math
                                .round(Utils.toDegrees(Math.atan2(otherPos.getY() - bot.getPosition().y,
                                                otherPos.getX() - bot.getPosition().getX())));
                return (direction + 360) % 360;
        }

        private double getDistanceBetween(GameObject otherObject) {
                return Utils.getDistanceBetween(this.bot, otherObject);
        }

        private double getDistanceBetween(Position otherPos) {
                return Utils.getDistanceBetween(this.bot.getPosition(), otherPos);
        }

}
