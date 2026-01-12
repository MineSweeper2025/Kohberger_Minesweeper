package htl.steyr.minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.w3c.dom.css.Counter;


public class GameFieldController extends Thread {

    public Label gameOverLabel;
    public Label timeLabelfinish;
    @FXML
    private Label timeLabel;

    @FXML
    private GridPane gamepane;

    private boolean[][] mines;
    private Button[][] buttons;


    @FXML
    private void initialize() {
        startTimer();
    }


    public void startTimer() {
        int stopcounter = 0;
        stopcounter++;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        final int[] counter = {0};

        if (stopcounter == 1) {
            KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
                counter[0]++;
                timeLabel.setText(String.valueOf(counter[0]));
            });

            timeline.getKeyFrames().add(frame);
            timeline.play();
        } else if (stopcounter == 2) {
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
        if (r < 0 || r >= maxR || c < 0 || c >= maxC || buttons[r][c].isDisable()) {
            return;
        }

        if (mines[r][c]) {
            buttons[r][c].setText("💣");
            buttons[r][c].setStyle("-fx-background-color: red;");
            gamepane.setDisable(true);
            gameOverLabel.setVisible(true);
            timeLabelfinish.setText(timeLabel.getText());
            timeLabelfinish.setVisible(true);
            timeLabel.setVisible(false);
            r = 0;
            c = 0;
            while (c < maxC && r < maxR) {
                if (mines[r][c]) {
                    buttons[r][c].setText("💣");
                    buttons[r][c].setStyle("-fx-background-color: red;");
                }
                c++;
                if (c == maxC - 1) {
                    r++;
                    c = 0;
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
            if(mouseEvent.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                if(buttons[r][c].getText() == "🚩"){
                    buttons[r][c].setText("");
                } else {
                    buttons[r][c].setText("🚩");
                }
            }




        }
    }



