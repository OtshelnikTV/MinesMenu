package minesweeper.mines;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Random;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class MinesweeperGame extends HelloApplication{
    private static final int BOARD_SIZE = 10;
    private static final int NUM_MINES = 10;
    private static final int TOTAL_MINES = 10;
    private static final int CELL_SIZE = 30; // Замените значение 30 на желаемый размер клетки

    private Cell[][] cells;
    private Button[][] buttons;

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        buttons = new Button[BOARD_SIZE][BOARD_SIZE];
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];

        // Создаем кнопки и добавляем их на сетку
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = new Button();
                button.setMinSize(30, 30);
                buttons[row][col] = button;
                root.add(button, col, row);
                root.setOnMousePressed(event -> {
                    if (event.isSecondaryButtonDown()) {
                        handleRightClick(event);
                    }
                });
                Cell cell = new Cell();
                cells[row][col] = cell;

                // Обработка клика на клетку
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> handleCellClick(finalRow, finalCol));

            }
        }
        // Расставляем мины
        placeMines();
        // Вычисляем количество соседних мин
        calculateNeighborMines();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void handleCellClick(int row, int col) {
        Cell cell = cells[row][col];

        // Проверяем, является ли клетка миной
        if (cell.isMine()) {
            // Игрок попал на мину, завершаем игру
            // Игрок попал на мину, завершаем игру
            gameOver();
        } else {
            // Клетка не содержит мину, открываем ее
            revealCell(row, col);
        }
    }
    private void handleRightClick(MouseEvent event) {

        int row = (int) (event.getY() / CELL_SIZE);
        int col = (int) (event.getX() / CELL_SIZE);

        if (isValidCell(row, col)) {
            Cell cell = cells[row][col];

            // Проверяем, является ли клетка уже открытой
            if (!cell.isRevealed()) {
                // Показываем контекстное меню для пометки клетки-мины
                ContextMenu contextMenu = new ContextMenu();
                MenuItem markMineItem = new MenuItem("Пометить как мину");
                markMineItem.setOnAction(e -> markCellAsMine(cell));
                contextMenu.getItems().add(markMineItem);
                contextMenu.show(buttons[row][col], event.getScreenX(), event.getScreenY());
            }
        }
    }
    private void markCellAsMine(Cell cell) {
        if (!cell.isMarkedAsMine()) {
            cell.setMarkedAsMine(true);
            // Обновить внешний вид клетки при пометке как мины
            Button button = buttons[cell.getRow()][cell.getCol()];
            button.setStyle("-fx-background-color: yellow;"); // Изменить цвет фона клетки на желтый
            updateRemainingMineCount();
        } else {
            cell.setMarkedAsMine(false);
            // Обновить внешний вид клетки при снятии пометки мины
            Button button = buttons[cell.getRow()][cell.getCol()];
            button.setStyle(""); // Сбросить стиль кнопки к стандартному
            updateRemainingMineCount();
        }
    }
    private void checkWinCondition() {
        boolean allCellsRevealed = true;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = cells[row][col];

                // Если есть клетка без открытия и без мины, игрок еще не выиграл
                if (!cell.isRevealed() && !cell.isMine()) {
                    allCellsRevealed = false;
                    break;
                }
            }
        }

        if (allCellsRevealed) {
            // Игрок выиграл, показываем сообщение о победе
            gameWon();
        }
    }
    private void gameWon() {
        // Показываем сообщение о победе
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("You won the game!");
        alert.showAndWait();

        // Добавьте здесь логику для завершения игры
        // ...
    }
    private void updateRemainingMineCount() {
        // Подсчитываем количество оставшихся мин
        int remainingMines = TOTAL_MINES;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = cells[row][col];

                // Если клетка помечена как мина, уменьшаем количество оставшихся мин
                if (cell.isMine() && cell.isRevealed()) {
                    remainingMines--;
                }
            }
        }

        // Обновляем отображение количества оставшихся мин
        // ...
    }
    private void gameOver() {
        // Показываем сообщение о поражении
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("You hit a mine! Game over.");
        alert.showAndWait();

        // Добавьте здесь логику для завершения игры
        // ...
    }
    private void revealCell(int row, int col) {
        Cell cell = cells[row][col];
        Button button = buttons[row][col];

        // Проверяем, была ли клетка уже открыта
        if (cell.isRevealed()) {
            return;
        }

        // Открываем клетку
        cell.setRevealed(true);
        button.setDisable(true);

        // Добавьте здесь логику для обновления внешнего вида клетки
        // ...

        // Если клетка пустая, открываем соседние пустые клетки рекурсивно
        if (cell.getNeighborMines() == 0) {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (isValidCell(i, j)) {
                        revealCell(i, j);
                    }
                }
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }
    private void placeMines() {
        Random random = new Random();

        int minesPlaced = 0;
        while (minesPlaced < NUM_MINES) {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);

            if (!cells[row][col].isMine()) {
                cells[row][col].setMine(true);
                minesPlaced++;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void calculateNeighborMines() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int count = 0;

                // Проверяем соседние клетки
                for (int i = row - 1; i <= row + 1; i++) {
                    for (int j = col - 1; j <= col + 1; j++) {
                        if (isValidCell(i, j) && cells[i][j].isMine()) {
                            count++;
                        }
                    }
                }

                // Устанавливаем количество соседних мин
                cells[row][col].setNeighborMines(count);
            }
        }
    }


}
