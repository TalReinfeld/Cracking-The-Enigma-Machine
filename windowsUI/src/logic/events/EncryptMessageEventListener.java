package logic.events;

import logic.events.handler.Invokable;

public interface EncryptMessageEventListener extends Invokable<String> {

    public void invoke(String arg);
}
