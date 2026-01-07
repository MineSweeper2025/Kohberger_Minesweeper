module htl.steyr.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires jdk.xml.dom;


    opens htl.steyr.minesweeper to javafx.fxml;
    exports htl.steyr.minesweeper;
}