package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import com.ctc.wstx.shaded.msv_core.reader.State;

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

    static boolean attack_next = false;
    static boolean continueFood = false;
    static int staTick = 0;

    public void computeNextPlayerAction(PlayerAction playerAction) {
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = new Random().nextInt(360);

        if (!gameState.getGameObjects().isEmpty()) {
            var foodList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var foodNotNearEdgeList = this.gameState.getGameObjects()
                .stream().filter(item -> (item.getGameObjectType() == ObjectTypes.FOOD || 
                item.getGameObjectType() == ObjectTypes.SUPERFOOD) && 
                (double)this.gameState.getWorld().radius.intValue() - 
                getDistanceBetween(item, this.gameState.getWorld()) >= 1.2 * 
                (double)this.bot.size.intValue()).sorted(Comparator.comparing(
                item -> this.getDistanceBetween(this.bot, (GameObject)item)))
                .collect(Collectors.toList());
  
            var playerList = gameState.getPlayerGameObjects()
                    .stream().filter(item -> item.getId() != this.bot.getId())
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
                    System.out.println("playerList: " + playerList);

            var wormholeList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.WORMHOLE)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var gasCloudList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var asteroidFieldList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var superFoodList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var supernovaPickupList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERNOVAPICKUP)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var supernovaBombList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERNOVABOMB)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var teleporterList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.TELEPORTER)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var shieldList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SHIELD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var torpedoSalvoList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.TORPEDOSALVO)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            var ticker = gameState.getWorld().getCurrentTick();
            System.out.println("Tick: " + ticker);


            var bordeRadius = gameState.world.getRadius();
            Position mid = new Position(0,0);
            GameObject dummy = new GameObject(null, null, null, null, mid, null);
            var botToMid = getDistanceBetween(this.bot, dummy);
            var nearestPlayer = getDistanceBetween(this.bot, playerList.get(0));

            double gasDist;
            int headGas;
            if (gasCloudList.size() > 0){
                gasDist = getDistanceBetween(this.bot, gasCloudList.get(0));
                headGas = getHeadingBetween(gasCloudList.get(0));
            } else {
                gasDist = 99999;
                headGas = -1;
            }

            double asteDist;
            int headAster;
            if (asteroidFieldList.size() > 0){
                asteDist = getDistanceBetween(this.bot, asteroidFieldList.get(0));
                headAster = getHeadingBetween(asteroidFieldList.get(0));
            } else {
                asteDist = 99999;
                headAster = -1;
            }

            var headPlayer = getHeadingBetween(playerList.get(0));
            System.out.println("Gas: " + gasDist + " Aster: " + asteDist + " Player: " + nearestPlayer + " Mid: " + botToMid + "size: " + this.bot.size); 
            var headMid = getHeadingBetween(dummy);
            double foodFarEdgeDist = 99999;
            int foodFarEdgeHead = -1;
            if (foodNotNearEdgeList.size() > 0){
                foodFarEdgeDist = getDistanceBetween(this.bot, foodNotNearEdgeList.get(0));
                foodFarEdgeHead = getHeadingBetween(foodNotNearEdgeList.get(0));
            } else {
                foodFarEdgeDist = 99999;
            }

            var botsize = this.bot.size;
            var enemySize = playerList.get(0).size;

            if (torpedoSalvoList.size() > 0){
                var salvoDist = getDistanceBetween(this.bot, torpedoSalvoList.get(0));
                var headSalvo = getHeadingBetween(torpedoSalvoList.get(0));
                System.out.println("Salvo: " + salvoDist + " Head: " + headSalvo);
                if ((salvoDist < 100 + 1.2 * botsize) && (botsize > 100)){
                    System.out.println("Bot is activating shield!");
                    playerAction.heading = foodFarEdgeHead;
                    playerAction.action = PlayerActions.ACTIVATESHIELD;
                } else if (salvoDist < 100 + 1.5 * botsize){
                    System.out.println("Bot is running from salvo!");
                    playerAction.heading = headSalvo + 120;
                    playerAction.action = PlayerActions.FORWARD;
                } else if (bordeRadius < botToMid + 1.2 * botsize){
                    System.out.println("Bot is running from border!");
                    playerAction.heading = headMid;
                    playerAction.action = PlayerActions.FORWARD;
                } else if ((nearestPlayer > enemySize + 250) && (foodNotNearEdgeList.size() > 0) && (foodFarEdgeDist < 400 + 5 * botsize)){
                    System.out.println("Bot is now chilling with no enemy nearby!");
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = foodFarEdgeHead;
                } else if ((nearestPlayer < 400 + 5 * enemySize) && (enemySize < botsize) && (enemySize > 0.2 * botsize) && (botsize > 40)){
                    System.out.println("Bot is attacking!");
                    playerAction.action = PlayerActions.FIRETORPEDOES;
                    playerAction.heading = headPlayer;
                } else if ((nearestPlayer < 400 + 5 * enemySize) && (enemySize < botsize)){
                    System.out.println("Bot is just chasing weak enemy!");
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = headPlayer;
                } else if ((nearestPlayer < 400 + 5 * enemySize) && (botsize > 0.4 * enemySize) && (botsize > 40)){
                    System.out.println("Bot is not scared at all!");
                    playerAction.action = PlayerActions.FIRETORPEDOES;
                    playerAction.heading = headPlayer;
                } else if (nearestPlayer < 400 + 5 * enemySize){
                    System.out.println("Bot is running for his life!");
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = (headPlayer + 180) % 360;
                } else if (gasDist < 100 + 1.2 * botsize){
                    playerAction.heading = headGas + 120;
                    playerAction.action = PlayerActions.FORWARD;
                } else if (asteDist < 100 + 1.2 * botsize){
                    playerAction.heading = headAster + 120;
                    playerAction.action = PlayerActions.FORWARD;
                } else if ((foodNotNearEdgeList.size() > 0) && (foodFarEdgeDist > 400 + 5 * botsize)){
                    System.out.println("Bot is now teleporting!");
                    playerAction.action = PlayerActions.FIRETELEPORT;
                        playerAction.heading = foodFarEdgeHead;
                } else {
                    System.out.println("Bot is now chilling with no enemy nearby!");
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = foodFarEdgeHead;
                }
            } else if (bordeRadius < botToMid + 1.2 * botsize){
                System.out.println("Bot is running from border!");
                playerAction.heading = headMid;
                playerAction.action = PlayerActions.FORWARD;
            } else if ((nearestPlayer > enemySize + 250) && (foodNotNearEdgeList.size() > 0) && (foodFarEdgeDist < 400 + 5 * botsize)){
                System.out.println("Bot is now chilling with no enemy nearby!");
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = foodFarEdgeHead;
            } else if ((nearestPlayer < 400 + 5 * enemySize) && (enemySize < botsize) && (enemySize > 0.2 * botsize) && (botsize > 40)){
                System.out.println("Bot is attacking!");
                playerAction.action = PlayerActions.FIRETORPEDOES;
                playerAction.heading = headPlayer;
            } else if ((nearestPlayer < 400 + 5 * enemySize) && (enemySize < botsize)){
                System.out.println("Bot is just chasing weak enemy!");
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = headPlayer;
            } else if ((nearestPlayer < 400 + 5 * enemySize) && (botsize > 0.4 * enemySize) && (botsize > 40)){
                System.out.println("Bot is not scared at all!");
                playerAction.action = PlayerActions.FIRETORPEDOES;
                playerAction.heading = headPlayer;
            } else if (nearestPlayer < 400 + 5 * enemySize){
                System.out.println("Bot is running for his life!");
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = (headPlayer + 180) % 360;
            } else if (gasDist < 100 + 1.2 * botsize){
                playerAction.heading = headGas + 120;
                playerAction.action = PlayerActions.FORWARD;
            } else if (asteDist < 100 + 1.2 * botsize){
                playerAction.heading = headAster + 120;
                playerAction.action = PlayerActions.FORWARD;
            } else if ((foodNotNearEdgeList.size() > 0) && (foodFarEdgeDist > 400 + 5 * botsize)){
                System.out.println("Bot is now teleporting!");
                playerAction.action = PlayerActions.FIRETELEPORT;
                    playerAction.heading = foodFarEdgeHead;
            } else {
                System.out.println("Bot is now chilling with no enemy nearby!");
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = foodFarEdgeHead;
            }

            this.playerAction = playerAction;
        }
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateSelfState();
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

    private double getDistanceBetween(GameObject object1, GameObject object2) {
        var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    private double getDistanceBetween(GameObject object1, World object2) {
        var radSqr = Math.pow(object1.getPosition().x, 2) + Math.pow(object1.getPosition().y, 2);
        var rad = Math.sqrt(radSqr);
        return object2.getRadius() - rad;
    }

    private int getHeadingBetween(GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }


}
