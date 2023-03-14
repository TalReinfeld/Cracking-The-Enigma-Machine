package logic.events.handler;

public interface Invokable<T> {

    void invoke(T arg);
}
