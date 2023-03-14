package components.body.main;

import javafx.beans.property.StringProperty;
import machineDtos.EngineDTO;

public interface encryptParentController {

    public EngineDTO getEngineDetails();

    public StringProperty getMachineEncryptedMessageProperty();

    }
