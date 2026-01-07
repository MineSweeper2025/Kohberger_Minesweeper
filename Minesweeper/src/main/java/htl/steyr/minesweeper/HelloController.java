package htl.steyr.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class HelloController {
    int difficulty = 0;

    @FXML
    private void initialize() {
    }

    public void onAnfaenger(ActionEvent actionEvent) {
        difficulty = 1;
        switchToGameField(actionEvent);
    }

    public void onFortgeschritten(ActionEvent actionEvent) {
        difficulty = 2;
        switchToGameField(actionEvent);
    }

    public void onProfi(ActionEvent actionEvent) {
        difficulty = 3;
        switchToGameField(actionEvent);
    }

    private void switchToGameField(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-field-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            

            GameFieldController controller = fxmlLoader.getController();
            controller.setDifficulty(this.difficulty);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
