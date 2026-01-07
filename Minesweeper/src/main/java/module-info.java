module htl.steyr.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;


    opens htl.steyr.minesweeper to javafx.fxml;
    exports htl.steyr.minesweeper;
}