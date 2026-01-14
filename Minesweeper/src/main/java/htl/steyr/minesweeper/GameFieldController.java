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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.css.Counter;

import java.io.IOException;


public class GameFieldController {

    public Label gameOverLabel;
    public Button resetButton;
    @FXML
    private Label timeLabel;

    @FXML
    private GridPane gamepane;

    private boolean[][] mines;
    private Button[][] buttons;
    private Timeline timeline;
    private int secondsPassed = 0;


    @FXML
    private void initialize() {
        setupTimer();
        startTimer();
    }

    private void setupTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsPassed++;
            timeLabel.setText(String.format("%03d", secondsPassed));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void startTimer() {
        secondsPassed = 0;
        timeLabel.setText("000");
        timeline.playFromStart();
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    int setDifficulty(int difficulty) {
        int minesdificulty = 0;
        switch (difficulty) {
            case 1:
                generateGrid(8, 8, 10);
                break;
            case 2:
                generateGrid(16, 16, 40);
                break;

            case 3:
                generateGrid(30, 16, 99);
                break;
        }
        return minesdificulty;
    }

    public void generateGrid(int collums, int rows, int minesdificulty) {
        gamepane.getChildren().clear();
        buttons = new Button[rows][collums];
        mines = new boolean[rows][collums];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < collums; col++) {
                Button btn = new Button();
                btn.setPrefSize(30, 30);

                int finalRow = row;
                int finalCol = col;
                btn.setOnAction(e -> revealField(rows, collums, finalRow, finalCol));
                btn.setOnMouseClicked(e -> placeFlag(e, finalRow, finalCol));

                buttons[row][col] = btn;
                gamepane.add(btn, col, row);
            }
        }
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

    private void revealField(int maxR, int maxC, int r, int c) {
        if (r < 0 || r >= maxR || c < 0 || c >= maxC || buttons[r][c].isDisable() || "🚩".equals(buttons[r][c].getText())) {
            return;
        }

        if (mines[r][c]) {
            stopTimer();
            gamepane.setDisable(true);
            gameOverLabel.setVisible(true);
            resetButton.setVisible(true);


            for (int row = 0; row < maxR; row++) {
                for (int col = 0; col < maxC; col++) {
                    if (mines[row][col]) {
                        buttons[row][col].setText("💣");
                        buttons[row][col].setStyle("-fx-background-color: red;");
                    }
                }
            }
        } else {
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

            buttons[r][c].setText(count > 0 ? String.valueOf(count) : "");
            buttons[r][c].setDisable(true);
            buttons[r][c].setStyle("-fx-background-color: lightgray; -fx-opacity: 1.0;");

            if (count == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        revealField(maxR, maxC, r + i, c + j);
                    }
                }
            }
        }
    }


    public void placeFlag(MouseEvent mouseEvent, int r, int c) {
        if (mouseEvent.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
            if (buttons[r][c].getText() == "🚩") {
                buttons[r][c].setText("");
            } else {
                buttons[r][c].setText("🚩");
            }
        }


    }

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
}
