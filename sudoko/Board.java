package sudoko;

import java.util.*;


// Contains board state and variables related to creation of it. Only accessible through the constructor.
public class Board {
    // Indicates how much work should be done to make the solution harder
    private int _hardness;
    // How large the board is in width and height (always a square)
    private int _boardSize;
    // Contains the state of the board, 0 indicates an empty position
    private ArrayList<Integer> _board;

    // Helper function, returns the i:nth row
    private ArrayList<Integer> getRow(int i) {
        ArrayList<Integer> row = new ArrayList<>(Collections.nCopies(_boardSize, 0));
        for(int j = 0; j < _boardSize; j++) {
            row.set(j, _board.get(i*_boardSize + j));
        }
        return row;
    }

    // Helper function, returns the i:nth column
    private ArrayList<Integer> getColumn(int i) {
        ArrayList<Integer> column = new ArrayList<>(Collections.nCopies(_boardSize, 0));
        for(int j = 0; j < _boardSize; j++) {
            column.set(j, _board.get(i + j*_boardSize));
        }
        return column;
    }

    // Finds a valid solved board state through brute force, in theory O(infinity) but in practice not because of the pseudo randomness of random
    private ArrayList<Integer> generateRandomSolution(Random random) {
        _board = new ArrayList<>(Collections.nCopies(_boardSize*_boardSize, 0));
        for(int i = 0; i < _boardSize; i++) {
            for(int j = 0; j < _boardSize; j++) {
                ArrayList<Integer> possibleValues = new ArrayList<>(_boardSize);
                for (int k = 0; k < _boardSize; k++) {
                    possibleValues.add(k, k + 1);
                }

                possibleValues.removeAll(getColumn(i));
                possibleValues.removeAll(getRow(j));

                if(possibleValues.size() != 0) {
                    _board.set(i + j * _boardSize, possibleValues.get(random.nextInt(possibleValues.size())));
                }
                else {
                    // relies on tail recursion optimisation to not blow the stack, be careful
                    return generateRandomSolution(random);
                }
            }
        }
        return _board;
    }

    // Tries if the board state can be advanced without the value at index i
    private boolean isSolvableWithout(int i) {
        int row = i / _boardSize;
        int column = i % _boardSize;
        Set<Integer> notPossibleValues = new LinkedHashSet<Integer>();
        notPossibleValues.addAll(getRow(row));
        notPossibleValues.addAll(getColumn(column));
        notPossibleValues.remove(_board.get(i));
        if(notPossibleValues.size() == _boardSize - 1) {
            return true;
        }
        else {
            return false;
        }
    }

    // Takes a fully solved board state and removes as many values as it can while still maintaining solvability. Is probabalistic and will try a number of times equal to the value of hardness
    private ArrayList<Integer> removePartOfTheSolution(Random random) {
        for(int i = 0; i < _hardness; i++) {
            int toBeRemoved = random.nextInt(_board.size());
            if(_board.get(toBeRemoved) != 0 && isSolvableWithout(toBeRemoved)) {
                _board.set(toBeRemoved, 0);
            }
        }
        return _board;
    }

    // Creates a board that can be solved of width and height indicated by boardSize, and tries to make it harder to solve depending on how high the value of hardness is (will try to make it harder an equal amount of times to the value of hardness, 0 results in a fully solved board state)
    public Board(int boardSize, int hardness) {
        _boardSize = boardSize;
        _hardness = hardness;

        Random random = new Random();
        _board = generateRandomSolution(random);
        _board = removePartOfTheSolution(random);
    }

    // For easier display of internal board state this function returns all the numbers contained in the internal board state formated as a string
    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < _boardSize; i++) {
            result += getRow(i).toString();
        }
        return result;
    }
}
