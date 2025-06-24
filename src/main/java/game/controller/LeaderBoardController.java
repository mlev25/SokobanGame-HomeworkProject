package game.controller;

import game.datautil.Player;
import game.datautil.PlayerDataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LeaderBoardController {
    @FXML
    private VBox playerViewBox;

    @FXML
    private Button closeButton;

    @FXML
    private void initialize(){
        List<Player> players = PlayerDataManager.getFinishedPlayersSorted();
        for (Player player : players) {
            Label label = new Label(player.toString());
            playerViewBox.getChildren().add(label);
        }

        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    public void handleClose() {}
}
