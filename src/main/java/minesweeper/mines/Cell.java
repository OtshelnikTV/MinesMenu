package minesweeper.mines;

public class Cell {
    private int row;
    private int col;
    private boolean isMine;
    private boolean isRevealed;
    private int neighborMines;
    private boolean mine;
    private boolean revealed;
    private boolean markedAsMine;

    public Cell() {
        isMine = false;
        isRevealed = false;
        neighborMines = 0;
    }
    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public int getNeighborMines() {
        return neighborMines;
    }

    public void setNeighborMines(int count) {
        neighborMines = count;
    }
    public boolean isMarkedAsMine() {
        return markedAsMine;
    }

    public void setMarkedAsMine(boolean markedAsMine) {
        this.markedAsMine = markedAsMine;
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

