package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConversionTable implements Serializable, Cloneable {

    private final List<Character> rightValues = new ArrayList<>();
    private final List<Character> leftValues = new ArrayList<>();

    public synchronized Character getRightCharacter(int position){

        return rightValues.get(position);
    }

    public synchronized Character getLeftCharacter(int position){

        return leftValues.get(position);
    }

    public synchronized int getCharIndexInRight(char character){

        return rightValues.indexOf(character);
    }

    public synchronized int getCharIndexInLeft(char character){

        return leftValues.indexOf(character);
    }

    public synchronized int getTableSize(){
        return rightValues.size();
    }

    public synchronized void add(Character right , Character left){

        rightValues.add(right);
        leftValues.add(left);
    }

    @Override
    public ConversionTable clone() {
        try {
            ConversionTable clone = (ConversionTable) super.clone();
            clone.rightValues.addAll(rightValues);
            clone.leftValues.addAll(leftValues);

            return clone;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
