package components;

import javafx.util.Pair;

import java.io.Serializable;

public class Rotor implements Convertor<Integer>, Serializable, Cloneable {

    private final int id;
    private final int notchPosition;
    private final ConversionTable rotorConversions;
    private boolean rightToLeft = true;

    public Rotor(int idValue, int notchValue, ConversionTable conversionTable){

        id = idValue;
        notchPosition = notchValue;
        rotorConversions = conversionTable;
    }

    public int getId() {

        return id;
    }

    public int getNotchPosition() {

        return notchPosition;
    }

    public int getConversionTableSize(){

        return rotorConversions.getTableSize();
    }

    public int getPositionOfChar(char c) {

        return rotorConversions.getCharIndexInRight(c);
    }

    public char getCharInPosition(int position) {

        return rotorConversions.getRightCharacter(position);
    }

    public Pair<Character,Character> getPosition(int index){

        return new Pair<Character,Character>(rotorConversions.getLeftCharacter(index),rotorConversions.getRightCharacter(index));
    }

    @Override
    public Integer convert(Integer position) {

        int nextPosition = rightToLeft ? rotorConversions.getCharIndexInLeft(rotorConversions.getRightCharacter(position)) :
                rotorConversions.getCharIndexInRight(rotorConversions.getLeftCharacter(position));

        rightToLeft = !rightToLeft;

        return nextPosition;
    }

    @Override
    public Rotor clone() {

        Rotor clone = new Rotor(id, notchPosition, rotorConversions);
        clone.rightToLeft = rightToLeft;

        return clone;
    }
}
