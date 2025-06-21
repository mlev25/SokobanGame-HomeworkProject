package game;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.tinylog.Logger;


public class WelcomeController {

    @FXML
    private TextField playerName;

    @FXML
    private void handleStart(){
        String name = playerName.getText().trim();
        if (name.isEmpty()){
            Logger.warn("Please enter your name!");
            return;
        }
        try {
            PlayerDataManager.addNewPlayer(name);
            SokobanApplication.switchToGame(name);
        } catch (Exception e) {
            Logger.error("Failed to load the scene: "+ e.getMessage());
        }

    }

}
