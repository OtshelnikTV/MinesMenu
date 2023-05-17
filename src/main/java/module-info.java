module minesweeper.mines {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens minesweeper.mines to javafx.fxml;
    exports minesweeper.mines;
}