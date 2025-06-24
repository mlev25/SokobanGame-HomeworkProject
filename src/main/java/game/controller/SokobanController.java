package game.controller;

import game.datautil.PlayerDataManager;
import game.conrollerutil.Coordinate;
import game.conrollerutil.GameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;


public class SokobanController {

    private String currentPlayerName;
    private static GameState initialState;
    private Stack<GameState> history = new Stack<>();
    private static List<Coordinate> customCords = new ArrayList<>();
    private static int playerStepCounter = 0;
    private int playerRow = 1;
    private int playerCol = 1;
    private boolean isOver = false;
    private static final List<Coordinate> goalCords = List.of(
            new Coordinate(3,7),
            new Coordinate(4,7),
            new Coordinate(5,7)
    );

    static {
        customCords.add(new Coordinate(0,0));
        customCords.add(new Coordinate(0,1));
        customCords.add(new Coordinate(0,2));
        customCords.add(new Coordinate(0,3));
        customCords.add(new Coordinate(0,4));
        customCords.add(new Coordinate(1,0));
        customCords.add(new Coordinate(2,0));
        customCords.add(new Coordinate(3,0));
        customCords.add(new Coordinate(4,0));
        customCords.add(new Coordinate(4,1));
        customCords.add(new Coordinate(4,2));
        customCords.add(new Coordinate(5,1));
        customCords.add(new Coordinate(5,2));
        customCords.add(new Coordinate(6,1));
        customCords.add(new Coordinate(7,1));
        customCords.add(new Coordinate(8,1));
        customCords.add(new Coordinate(8,2));
        customCords.add(new Coordinate(8,3));
        customCords.add(new Coordinate(8,4));
        customCords.add(new Coordinate(8,5));
        customCords.add(new Coordinate(7,5));
        customCords.add(new Coordinate(6,5));
        customCords.add(new Coordinate(7,6));
        customCords.add(new Coordinate(7,7));
        customCords.add(new Coordinate(7,8));
        customCords.add(new Coordinate(6,8));
        customCords.add(new Coordinate(5,8));
        customCords.add(new Coordinate(4,8));
        customCords.add(new Coordinate(3,8));
        customCords.add(new Coordinate(2,8));
        customCords.add(new Coordinate(2,7));
        customCords.add(new Coordinate(2,6));
        customCords.add(new Coordinate(3,6));
        customCords.add(new Coordinate(4,6));
        customCords.add(new Coordinate(4,5));
        customCords.add(new Coordinate(4,4));
        customCords.add(new Coordinate(3,4));
        customCords.add(new Coordinate(2,4));
        customCords.add(new Coordinate(1,4));
    }

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label numOfSteps;

    @FXML
    private Label currCell;


    @FXML
    public void initialize(){
        Logger.info("Initializing the game field...");
        updateCurrentCell();
        updateCounter();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                StackPane pane = new StackPane();
                pane.setStyle("-fx-border-color: gray; -fx-background-color: white;");
                gameGrid.add(pane, i, j);
            }
        }

        for (Coordinate coord : customCords) {
            getNodeByRowColumnIndex(coord.row(), coord.col(), gameGrid).ifPresent(pane -> pane.setStyle("-fx-border-color: black; -fx-background-color: black;"));
        }

        StackPane stickManPane = getNodeByRowColumnIndex(1,1,gameGrid).orElse(null);
        StackPane pane1 = getNodeByRowColumnIndex(2,2,gameGrid).orElse(null);
        StackPane pane2 = getNodeByRowColumnIndex(2,3,gameGrid).orElse(null);
        StackPane pane3 = getNodeByRowColumnIndex(3,2,gameGrid).orElse(null);

        if (pane1 != null && pane2 != null && pane3 != null && stickManPane != null){
            stickManPane.getChildren().add(createImageView("/images/stickman.png"));
            pane1.getChildren().add(createImageView("/images/ball.png"));
            pane2.getChildren().add(createImageView("/images/ball.png"));
            pane3.getChildren().add(createImageView("/images/ball.png"));
        }

        //set the goal fields
        StackPane goal1 = getNodeByRowColumnIndex(3, 7, gameGrid).orElse(new StackPane());
        StackPane goal2 = getNodeByRowColumnIndex(4, 7, gameGrid).orElse(new StackPane());
        StackPane goal3 = getNodeByRowColumnIndex(5, 7, gameGrid).orElse(new StackPane());

        goal1.setStyle("-fx-border-color: red; -fx-background-color: yellow;");
        goal2.setStyle("-fx-border-color: red; -fx-background-color: yellow;");
        goal3.setStyle("-fx-border-color: red; -fx-background-color: yellow;");
        //System.out.println("Stickman pane children: " + stickManPane.getChildren());

        Logger.info("Initialized the game field!");

        saveState();
        initialState = history.getFirst();
    }

    private ImageView createImageView(String path) {
        Image image = new Image(getClass().getResource(path).toExternalForm());
        ImageView view = new ImageView(image);
        view.setFitWidth(64);
        view.setFitHeight(58);
        view.setPreserveRatio(true);
        return view;
    }

    private Optional<StackPane> getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            if (rowIndex == null) rowIndex = 0;
            if (colIndex == null) colIndex = 0;
            if (rowIndex == row && colIndex == column && node instanceof StackPane) {
                return Optional.of((StackPane) node);
            }
        }
        return Optional.empty();
    }


    public void movePlayer(int dx, int dy) {
        int newRow = playerRow + dy;
        int newCol = playerCol + dx;

        if (customCords.contains(new Coordinate(newRow, newCol))) {
            Logger.info(" You cannot move into a wall.");
            return;
        }

        Optional<StackPane> newPaneOpt = getNodeByRowColumnIndex(newRow, newCol, gameGrid);
        Optional<StackPane> currentPaneOpt = getNodeByRowColumnIndex(playerRow, playerCol, gameGrid);

        if (newPaneOpt.isEmpty() || currentPaneOpt.isEmpty()) {
            Logger.warn("No pane found!");
            return;
        }

        StackPane newPane = newPaneOpt.get();
        StackPane currentPane = currentPaneOpt.get();

        Optional<ImageView> ballOpt = newPane.getChildren().stream()
                .filter(node -> node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("ball"))
                .map(node -> (ImageView) node)
                .findFirst();

        if (ballOpt.isPresent()) {

            int pushRow = newRow + dy;
            int pushCol = newCol + dx;

            if (customCords.contains(new Coordinate(pushRow, pushCol))) {
                Logger.info("Cannot push ball into a wall.");
                return;
            }

            Optional<StackPane> pushPaneOpt = getNodeByRowColumnIndex(pushRow, pushCol, gameGrid);
            if (pushPaneOpt.isEmpty() || !pushPaneOpt.get().getChildren().isEmpty()) {
                Logger.info("Cannot push ball into specified location. There could be a wall or another ball.");
                return;
            }

            ballPush(newRow, newCol, pushRow, pushCol);


        }

        Optional<ImageView> playerOpt = currentPane.getChildren().stream()
                .filter(node -> node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("stickman"))
                .map(node -> (ImageView) node)
                .findFirst();


        playerOpt.ifPresent(player -> {
            currentPane.getChildren().remove(player);
            newPane.getChildren().add(player);
            saveState();
            playerRow = newRow;
            playerCol = newCol;
            playerStepCounter++;
            updateCounter();
            updateCurrentCell();
            Logger.info("Player moved to ({}, {})", newRow, newCol);
            checkGoal();
            if (isOver) {
                showVictoryPopup();
                restoreCords(initialState);
                playerRow = initialState.playerRow;
                playerCol = initialState.playerCol;
                playerStepCounter = 0;
                updateCounter();
                updateCurrentCell();
                history.clear();
            }
        });
    }

    private void ballPush(int fromRow, int fromCol, int toRow, int toCol) {
        Optional<StackPane> fromPaneOpt = getNodeByRowColumnIndex(fromRow, fromCol, gameGrid);
        Optional<StackPane> toPaneOpt = getNodeByRowColumnIndex(toRow, toCol, gameGrid);

        if (fromPaneOpt.isEmpty() || toPaneOpt.isEmpty()) {
            return;
        }

        StackPane fromPane = fromPaneOpt.get();
        StackPane toPane = toPaneOpt.get();

        Optional<ImageView> ballOpt = fromPane.getChildren().stream()
                .filter(node -> node instanceof ImageView && ((ImageView) node).getImage().getUrl().contains("ball"))
                .map(node -> (ImageView) node)
                .findFirst();

        ballOpt.ifPresent(ball -> {
            fromPane.getChildren().remove(ball);
            toPane.getChildren().add(ball);
            Logger.info("Ball pushed to ({}, {})", toRow, toCol);

        });
    }

    public void checkGoal(){
        for (Coordinate goalCord : goalCords) {
            Optional<StackPane> goalPane = getNodeByRowColumnIndex(goalCord.row(), goalCord.col(), gameGrid);

            if (goalPane.isEmpty()) return;

            boolean currentPaneHasBall = goalPane.get().getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .anyMatch(img -> ((ImageView) img).getImage().getUrl().contains("ball"));

            if (!currentPaneHasBall) {
                isOver = false;
                return;
            }
        }

        isOver = true;
        Logger.info("Congratulations, You Won! All balls are on goals.");
    }

    public void updateCounter(){
        Logger.info("Counter updated!");
        numOfSteps.setText(String.format("Nr. of steps: %d", playerStepCounter));
    }

    public void updateCurrentCell(){
        Logger.info("Cell tracer updated!");
        currCell.setText(String.format("Current cell {%d, %d}", playerRow, playerCol));
    }

    private void showVictoryPopup() {
        PlayerDataManager.updatePlayerScore(currentPlayerName, playerStepCounter);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Játék vége");
        alert.setHeaderText("Gratulálunk, nyertél!");
        alert.setContentText("Lépésszám: " + playerStepCounter);
        alert.showAndWait();

    }

    //Undo help functions

    private void saveState(){
        List<Coordinate> ballPositions = getAllBallCords();
        GameState gameState = new GameState(playerRow, playerCol, ballPositions);
        history.push(gameState);
    }


    private void clearImagesFromPane(StackPane pane, String imageName) {
        if (pane == null) return;
        pane.getChildren().removeIf(node ->
                node instanceof ImageView &&
                        ((ImageView) node).getImage().getUrl().contains(imageName)
        );
    }

    public void restoreCords(GameState prevGameState){
        int prevPlayerRow = prevGameState.playerRow;
        int prevPlayerCol = prevGameState.playerCol;
        List<Coordinate> prevBallPositions = prevGameState.ballPositions;
        List<Coordinate> currBallPositions = getAllBallCords();

        StackPane currStickManPane = getNodeByRowColumnIndex(playerRow,playerCol,gameGrid).orElse(null);
        StackPane currPane1 = getNodeByRowColumnIndex(currBallPositions.getFirst().row(),currBallPositions.getFirst().col(),gameGrid).orElse(null);
        StackPane currPane2 = getNodeByRowColumnIndex(currBallPositions.get(1).row(),currBallPositions.get(1).col(),gameGrid).orElse(null);
        StackPane currPane3 = getNodeByRowColumnIndex(currBallPositions.get(2).row(),currBallPositions.get(2).col(),gameGrid).orElse(null);

        if (currPane1 != null && currPane2 != null && currPane3 != null && currStickManPane != null){
            clearImagesFromPane(currStickManPane, "stickman");
            clearImagesFromPane(currPane1, "ball");
            clearImagesFromPane(currPane2, "ball");
            clearImagesFromPane(currPane3, "ball");
        }


        StackPane stickManPane = getNodeByRowColumnIndex(prevPlayerRow,prevPlayerCol,gameGrid).orElse(null);
        StackPane pane1 = getNodeByRowColumnIndex(prevBallPositions.getFirst().row(),prevBallPositions.getFirst().col(),gameGrid).orElse(null);
        StackPane pane2 = getNodeByRowColumnIndex(prevBallPositions.get(1).row(),prevBallPositions.get(1).col(),gameGrid).orElse(null);
        StackPane pane3 = getNodeByRowColumnIndex(prevBallPositions.get(2).row(),prevBallPositions.get(2).col(),gameGrid).orElse(null);

        if (pane1 != null && pane2 != null && pane3 != null && stickManPane != null){
            stickManPane.getChildren().add(createImageView("/images/stickman.png"));
            pane1.getChildren().add(createImageView("/images/ball.png"));
            pane2.getChildren().add(createImageView("/images/ball.png"));
            pane3.getChildren().add(createImageView("/images/ball.png"));
        }



    }

    private List<Coordinate> getAllBallCords() {
        List<Coordinate> ballCoords = new ArrayList<>();

        for (javafx.scene.Node node : gameGrid.getChildren()) {
            if (!(node instanceof StackPane)) continue;

            StackPane pane = (StackPane) node;

            boolean hasBall = pane.getChildren().stream()
                    .filter(child -> child instanceof ImageView)
                    .anyMatch(img -> ((ImageView) img).getImage().getUrl().contains("ball"));

            if (hasBall) {
                Integer row = GridPane.getRowIndex(pane);
                Integer col = GridPane.getColumnIndex(pane);
                if (row == null) row = 0;
                if (col == null) col = 0;
                ballCoords.add(new Coordinate(row, col));
            }
        }

        return ballCoords;
    }

    @FXML
    public void undo(){
        if (history.isEmpty()) return;

        GameState prev = history.pop();
        restoreCords(prev);
        playerRow = prev.playerRow;
        playerCol = prev.playerCol;
        if (playerStepCounter > 0){
            playerStepCounter--;
        }


        updateCounter();
        updateCurrentCell();
    }


    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void showLeaderBoard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/leaderboard.fxml"));
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Leaderboard");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // <-- Itt a lényeg
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
