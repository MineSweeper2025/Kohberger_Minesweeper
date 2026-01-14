package htl.steyr.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

/**
 * Controller für das Hauptmenü der Minesweeper-Anwendung.
 * Verwaltet die Auswahl des Schwierigkeitsgrades und leitet zum Spielfeld weiter.
 */
public class HelloController {
    // Aktuell gewählter Schwierigkeitsgrad (1=Anfänger, 2=Fortgeschritten, 3=Profi)
    int difficulty = 0;

    @FXML
    private void initialize() {
    }

    /**
     * Setzt den Schwierigkeitsgrad auf "Anfänger" und ruft die nächste Seite auf.
     * @param actionEvent Das auslösende Event
     */
    public void onAnfaenger(ActionEvent actionEvent) {
        difficulty = 1;
        switchToGameField(actionEvent);
    }

    /**
     * Setzt den Schwierigkeitsgrad auf "Fortgeschritten" und ruft die nächste Seite auf.
     * @param actionEvent Das auslösende Event
     */
    public void onFortgeschritten(ActionEvent actionEvent) {
        difficulty = 2;
        switchToGameField(actionEvent);
    }

    /**
     * Setzt den Schwierigkeitsgrad auf "Profi" ruft die nächste Seite auf.
     * @param actionEvent Das auslösende Event
     */
    public void onProfi(ActionEvent actionEvent) {
        difficulty = 3;
        switchToGameField(actionEvent);
    }

    /**
     * Lädt die game-field-view.fxml und übergibt den gewählten Schwierigkeitsgrad.
     * @param event Das Event, um das aktuelle Fenster (Stage) zu ermitteln
     */
    private void switchToGameField(ActionEvent event) {
        try {
            // FXMLLoader initialisieren und FXML-Datei laden
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-field-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            
            // Zugriff auf den Controller des Spielfelds, um Daten zu übergeben
            GameFieldController controller = fxmlLoader.getController();
            controller.setDifficulty(this.difficulty);

            // Aktuelle Stage abrufen und die neue Szene setzen
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
