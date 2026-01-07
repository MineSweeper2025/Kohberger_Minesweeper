package htl.steyr.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onAnfaenger(ActionEvent actionEvent) {
    }

    public void onFortgeschritten(ActionEvent actionEvent) {
    }

    public void onProfi(ActionEvent actionEvent) {
    }
}
