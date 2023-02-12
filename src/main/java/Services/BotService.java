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

                if (!gameState.getGameObjects().isEmpty()) {
                        List<GameObject> playerList = gameState.getGameObjects()
                                        .stream().filter(item -> item.getGameObjectType() == ObjectTypes.PLAYER)
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

                        Pair<Integer, Double> bestTarget = Shooting.ShootingTarget(playerList, bot);
                        Pair<Integer, Double> bestDirection = Direction.bestAngle(gameState.getGameObjects(), bot,
                                        gameState.getWorld().getRadius());
                        if (bestDirection.getFirst() * Config.offensiveRatio > bestTarget.getFirst()) {
                                playerAction.action = PlayerActions.FORWARD;
                                playerAction.heading = bestDirection.getFirst();
                        } else {
                                playerAction.heading = bestTarget.getFirst();
                                playerAction.action = PlayerActions.FIRETORPEDOES;
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
}
