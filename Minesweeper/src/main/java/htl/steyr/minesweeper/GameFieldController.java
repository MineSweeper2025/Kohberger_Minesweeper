package htl.steyr.minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.css.Counter;

import java.io.IOException;


/**
 * Hauptlogik des Minesweeper-Spiels.
 * Verwaltet das Spielfeld, die Minengenerierung, den Timer und die Benutzerinteraktionen.
 */
public class GameFieldController {

    public Label gameOverLabel;
    public Button resetButton;
    public AnchorPane AnchorPane;
    @FXML
    private Label timeLabel;

    @FXML
    private GridPane gamepane;

    //Array zur Speicherung der Minen-Positionen
    private boolean[][] mines;
    // Array der Buttons im UI
    private Button[][] buttons;
    // Timeline für die Verwaltung des Sekundenzählers
    private Timeline timeline;
    // Verstrichene Sekunden seit Spielstart
    private int secondsPassed = 0;
    // Anzahl der bereits sicher aufgedeckten Felder
    private int revealedCount = 0;
    //Checks if Admin Mode is on
    private boolean adminModeOn = false;


    @FXML
    private void initialize() {
        setupTimer();
        startTimer();
        AnchorPane.setOnKeyPressed(this::onAdminMode);
    }


    /**
     * Konfiguriert die Timeline, die jede Sekunde das Zeit-Label aktualisiert.
     */
    private void setupTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsPassed++;
            timeLabel.setText(String.format("%03d", secondsPassed));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Startet den Timer.
     */
    public void startTimer() {
        secondsPassed = 0;
        timeLabel.setText("000");
        timeline.playFromStart();
    }

    /**
     * Stoppt den Timer (z.B. bei Game Over).
     */
    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Konfiguriert die Grid-Größe und Minenanzahl basierend auf dem Schwierigkeitsgrad.
     *
     * @param difficulty Der gewählte Schwierigkeitsgrad (1-3)
     * @return minesdificulty, wert für die Anzahl der zu generierenden Minen
     */
    public void setDifficulty(int difficulty) {
        int minesdificulty = 0;
        int safe = 0;
        switch (difficulty) {
            case 1:
                generateGrid(8, 8, 10, 54);
                break;
            case 2:
                generateGrid(16, 16, 40, 216);
                break;
            case 3:
                generateGrid(30, 16, 99, 381);
                break;
        }
    }

    /**
     * Erstellt das visuelle Spielfeld und verteilt die Minen zufällig.
     *
     * @param collums        Anzahl der Spalten
     * @param rows           Anzahl der Zeilen
     * @param minesdificulty Anzahl der zu platzierenden Minen
     */
    public void generateGrid(int collums, int rows, int minesdificulty, int safe) {
        gamepane.getChildren().clear();
        buttons = new Button[rows][collums];
        mines = new boolean[rows][collums];
        revealedCount = 0;

        // Erstellung der Buttons und Zuweisung der Events
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < collums; col++) {
                Button btn = new Button();
                btn.setPrefSize(30, 30);

                int finalRow = row;
                int finalCol = col;
                // Linksklick zum Aufdecken
                btn.setOnAction(e -> revealField(rows, collums, finalRow, finalCol, safe));
                // Rechtsklick für Flaggen
                btn.setOnMouseClicked(e -> placeFlag(e, finalRow, finalCol));

                buttons[row][col] = btn;
                gamepane.add(btn, col, row);
            }
        }

        // Zufälliges Platzieren der Minen
        int placed = 0;
        while (placed < minesdificulty) {
            int r = (int) (Math.random() * rows);
            int c = (int) (Math.random() * collums);
            if (!mines[r][c]) {
                mines[r][c] = true;
                placed++;
            }
        }
    }

    /**
     * Logik zum Aufdecken eines Feldes. Beinhaltet die Überprüfung auf Minen
     * und die rekursive Aufdeckung leerer Nachbarfelder.
     *
     * @param maxR Maximale Zeilenanzahl
     * @param maxC Maximale Spaltenanzahl
     * @param r    Aktuelle Zeile
     * @param c    Aktuelle Spalte
     * @param safe Anzahl der sicheren Felder (ohne Minen)
     */
    private void revealField(int maxR, int maxC, int r, int c, int safe) {
        // Abbruchbedingungen: außerhalb des Grids, bereits aufgedeckt oder mit Flagge markiert
        if (r < 0 || r >= maxR || c < 0 || c >= maxC || buttons[r][c].isDisable() || "🚩".equals(buttons[r][c].getText())) {
            return;
        }


        if (mines[r][c]) {
            // Fall: Mine getroffen
            stopTimer();
            gamepane.setDisable(true);
            gameOverLabel.setVisible(true);
            resetButton.setVisible(true);

            // Alle Minen auf dem Feld anzeigen
            for (int row = 0; row < maxR; row++) {
                for (int col = 0; col < maxC; col++) {
                    if (mines[row][col]) {
                        buttons[row][col].setText("💣");
                        buttons[row][col].setStyle("-fx-background-color: red;");
                    }
                }
            }
        } else {
            // Fall: Kein Mine, Nachbarn zählen
            int count = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nr = r + i;
                    int nc = c + j;
                    if (nr >= 0 && nr < maxR && nc >= 0 && nc < maxC && mines[nr][nc]) {
                        count++;
                    }
                }
            }

            // Feld visuell deaktivieren und Zahl anzeigen
            revealedCount++;
            buttons[r][c].setText(count > 0 ? String.valueOf(count) : "");
            buttons[r][c].setDisable(true);
            buttons[r][c].setStyle("-fx-background-color: lightgray; -fx-opacity: 1.0;");


            //Wenn alle sicheren Felder aufgedeckt sind, gewinnt der Spieler
            if (safe == revealedCount) {
                stopTimer();
                gamepane.setDisable(true);
                gameOverLabel.setVisible(true);
                gameOverLabel.setText("You Win!");
                gameOverLabel.setStyle("-fx-text-fill: lime;");
                resetButton.setVisible(true);
            }

            // Wenn keine Mine in der Nachbarschaft ist, rekursiv weiter aufdecken
            if (count == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        revealField(maxR, maxC, r + i, c + j, safe);
                    }
                }
            }
        }
    }

    /**
     * Setzt oder entfernt eine Flagge per Rechtsklick.
     *
     * @param mouseEvent Das MouseEvent zur Bestimmung der Maustaste
     * @param r          Zeile des Buttons
     * @param c          Spalte des Buttons
     */
    public void placeFlag(MouseEvent mouseEvent, int r, int c) {
        if (mouseEvent.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
            if ("🚩".equals(buttons[r][c].getText())) {
                buttons[r][c].setText("");
            } else {
                buttons[r][c].setText("🚩");
            }
        }
    }

    /**
     * Beendet das aktuelle Spiel und kehrt zum Hauptmenü zurück.
     *
     * @param event Das ActionEvent vom Reset-Button
     */
    public void onReset(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * @param keyEvent Checks for Admin Key
     */
    public void onAdminMode(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        if (code == KeyCode.A && keyEvent.isControlDown()) {
            adminModeOn = !adminModeOn;  // Toggle the boolean
            for (int row = 0; row < mines.length; row++) {
                for (int col = 0; col < mines[0].length; col++) {
                    if (mines[row][col]) {
                        buttons[row][col].setText(adminModeOn ? "💣" : "");
                    }
                }
            }
        }
    }
}