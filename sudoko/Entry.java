package sudoko;

// Entry point for the program, contains main function
public class Entry {
    public static void main(String[] args) {
        int boardSize = 4;
        int hardness = 1000;
        Board board = new Board(boardSize, hardness);
        System.out.println(board);
    }
}
