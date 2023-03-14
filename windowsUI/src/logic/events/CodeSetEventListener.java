package logic.events;

import logic.events.handler.Invokable;
import machineDtos.EngineDTO;

public interface CodeSetEventListener extends Invokable<EngineDTO> {

    void invoke(EngineDTO updatedValue);
}
