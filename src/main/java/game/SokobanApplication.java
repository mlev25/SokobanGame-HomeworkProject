package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

public class SokobanApplication extends Application {

    public static Stage mainStage;

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcomescreen.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(scene);
        primaryStage.show();

        Logger.info("Welcome screen initialized.");
    }

    public static void switchToGame(String playerName) throws Exception {
        FXMLLoader loader = new FXMLLoader(SokobanApplication.class.getResource("/sokoban.fxml"));
        Scene scene = new Scene(loader.load());

        SokobanController controller = loader.getController();
        controller.setCurrentPlayerName(playerName);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> controller.movePlayer(0, -1);
                case DOWN -> controller.movePlayer(0, 1);
                case LEFT -> controller.movePlayer(-1, 0);
                case RIGHT -> controller.movePlayer(1, 0);
            }
        });

        mainStage.setTitle("Sokoban Game");
        mainStage.setScene(scene);
        mainStage.show();

        Logger.info("Game successfully started for player named {} ", playerName);
    }

    @Override
    public void stop(){
        PlayerDataManager.removeUnnecessaryPlayers();
        Logger.info("Application stopped");
    }

}
