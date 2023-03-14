package logic.events.handler;

import java.util.ArrayList;
import java.util.List;

public class MachineEventHandler<K extends Invokable> {

    private final List<K> listenersList = new ArrayList<>();

    public void addListener(K listener){

        listenersList.add(listener);
    }

    public void removeListener(K listener){

        listenersList.remove(listener);
    }

    public void fireEvent (Object updatedValue) {

        List<K> listenersToInvoke = new ArrayList<>(listenersList);
        for (K listener : listenersToInvoke) {
            listener.invoke(updatedValue);
        }
    }
}
