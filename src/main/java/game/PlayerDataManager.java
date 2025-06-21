package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.tinylog.Logger;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerDataManager {
    private static final String DATA_FILE = "players.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static PlayersList loadPlayersList() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new PlayersList();
        }

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, PlayersList.class);
        } catch (IOException e) {
            Logger.error("Failed to load players list: " + e.getMessage());
            return new PlayersList();
        }
    }

    public static void savePlayersList(PlayersList data) {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            Logger.error("Failed to save players list: " + e.getMessage());
        }
    }

    public static void addNewPlayer(String name) {
        PlayersList data = loadPlayersList();
        data.addPlayer(new Player(name, 0));
        savePlayersList(data);
    }

    public static void updatePlayerScore(String name, int newScore) {
        PlayersList data = loadPlayersList();
        List<Player> players = data.getPlayers();
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                p.setScore(newScore);
                break;
            }
        }
        savePlayersList(data);
    }
    public static List<Player> getFinishedPlayersSorted() {
        PlayersList data = loadPlayersList();
        return data.getPlayers().stream()
                .filter(p -> p.getScore() > 0)
                .sorted(Comparator.comparingInt(Player::getScore))
                .toList();
    }

    public static void removeUnnecessaryPlayers() {
        PlayersList playersList = loadPlayersList();
        List<Player> filtered = playersList.getPlayers().stream()
                .filter(p -> p.getScore() > 0)
                .collect(Collectors.toList());
        playersList.setPlayers(filtered);

        savePlayersList(playersList);
        Logger.info("Removed unnecessary players with 0 scores from the json file!");
    }
}
