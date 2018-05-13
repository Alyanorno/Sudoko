package sudoko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Field {
    private int _fieldSize;
    private ArrayList<Integer> _field;

    private ArrayList<Integer> getRow(int i) {
        ArrayList<Integer> row = new ArrayList<>(Collections.nCopies(_fieldSize, 0));
        for(int j = 0; j < _fieldSize; j++) {
            row.set(j, _field.get(i*_fieldSize + j));
        }
        return row;
    }

    private ArrayList<Integer> getColumn(int i) {
        ArrayList<Integer> column = new ArrayList<>(Collections.nCopies(_fieldSize, 0));
        for(int j = 0; j < _fieldSize; j++) {
            column.set(j, _field.get(i + j*_fieldSize));
        }
        return column;
    }

    private ArrayList<Integer> generateRandomSolution(Random random) {
        _field = new ArrayList<>(Collections.nCopies(_fieldSize*_fieldSize, 0));
        boolean failed = false;
        for(int i = 0; i < _fieldSize; i++) {
            for(int j = 0; j < _fieldSize; j++) {
                ArrayList<Integer> possibleValues = new ArrayList<>(_fieldSize);
                for (int k = 0; k < _fieldSize; k++) {
                    possibleValues.add(k, k + 1);
                }

                possibleValues.removeAll(getRow(j));
                possibleValues.removeAll(getColumn(i));

                if(possibleValues.size() != 0) {
                    _field.set(i + j * _fieldSize, possibleValues.get(random.nextInt(possibleValues.size())));
                }
                else {
                    failed = true;
                    break;
                }
            }
        }
        if(failed) {
            return generateRandomSolution(random);
        }
        else {
            return _field;
        }
    }

    private ArrayList<Integer> removePartOfTheSolution() {
        // TODO: Implement
        return _field;
    }

    public Field(int fieldSize) {
        _fieldSize = fieldSize;

        Random random = new Random();
        _field = generateRandomSolution(random);
        _field = removePartOfTheSolution();
    }

    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < _fieldSize; i++) {
            result += getRow(i).toString();
        }
        return result;
    }
}
