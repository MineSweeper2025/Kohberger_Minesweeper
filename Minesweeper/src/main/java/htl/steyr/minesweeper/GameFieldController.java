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


public class GameFieldController extends Thread{

    @FXML
    Label timeLabel = new Label();

    @FXML
    GridPane gamepane = new GridPane();

    @FXML
    private void initialize() {

    }

    public void Timer(Runnable runnable){
        int Counter = 0;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> timeLabel.setText(String.valueOf(Counter +1)))
                );


    }

    void setDifficulty(int difficulty) {
        switch (difficulty) {
            case 1:
                generateGrid(8, 8);
                break;
            case 2:
                generateGrid(16, 16);
                break;

            case 3:
                generateGrid(30, 16);
                break;
        }
    }

    public void generateGrid(int collums, int rows) {

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < collums; col++) {
                Button button = new Button();
                button.setPrefSize(30, 30);
                gamepane.add(button, col, row);
            }
        }

    }


}
