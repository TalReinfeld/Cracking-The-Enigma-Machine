package logic;

public class Counter {

    private int data = 1;

    public synchronized int get() {

        return data;
    }

    public synchronized void update() {

        data++;
    }
}
