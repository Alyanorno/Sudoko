package sudoko;

import java.util.*;


public class Board {
    private int _hardness;
    private int _boardSize;
    private ArrayList<Integer> _board;

    private ArrayList<Integer> getRow(int i) {
        ArrayList<Integer> row = new ArrayList<>(Collections.nCopies(_boardSize, 0));
        for(int j = 0; j < _boardSize; j++) {
            row.set(j, _board.get(i*_boardSize + j));
        }
        return row;
    }

    private ArrayList<Integer> getColumn(int i) {
        ArrayList<Integer> column = new ArrayList<>(Collections.nCopies(_boardSize, 0));
        for(int j = 0; j < _boardSize; j++) {
            column.set(j, _board.get(i + j*_boardSize));
        }
        return column;
    }

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
                    return generateRandomSolution(random);
                }
            }
        }
        return _board;
    }

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

    private ArrayList<Integer> removePartOfTheSolution(Random random) {
        for(int i = 0; i < _hardness; i++) {
            int toBeRemoved = random.nextInt(_board.size());
            if(_board.get(toBeRemoved) != 0 && isSolvableWithout(toBeRemoved)) {
                _board.set(toBeRemoved, 0);
            }
        }
        return _board;
    }

    public Board(int boardSize, int hardness) {
        _boardSize = boardSize;
        _hardness = hardness;

        Random random = new Random();
        _board = generateRandomSolution(random);
        _board = removePartOfTheSolution(random);
    }

    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < _boardSize; i++) {
            result += getRow(i).toString();
        }
        return result;
    }
}
