package htl.steyr.minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.w3c.dom.css.Counter;


public class GameFieldController extends Thread {

    @FXML
    Label timeLabel = new Label();

    @FXML
    GridPane gamepane = new GridPane();

    private boolean[][] mines;
    private Button[][] buttons;


    @FXML
    private void initialize() {
        startTimer();
    }


    public void startTimer() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        final int[] counter = {0};

        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
            counter[0]++;
            timeLabel.setText(String.valueOf(counter[0]));
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
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
                btn.setOnAction(e -> revealField(finalRow, finalCol, rows, collums));

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
        if (mines[r][c]) {
            buttons[r][c].setText("💣");
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
            buttons[r][c].setDisable(true); // Feld deaktivieren
        }
    }


}
