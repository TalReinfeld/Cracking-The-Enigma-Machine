package exceptions;

import java.text.MessageFormat;

public class ConvertorsInMachineOutOfRangeException extends Error {

    public ConvertorsInMachineOutOfRangeException(String objectName, int currAmount, int minAmount, int maxAmount){
        super(MessageFormat.format("You have {0} {1}.{2}You should have at least {3} {1} in the system for using the machine and at most {4}",
                currAmount,objectName,System.lineSeparator(),minAmount,maxAmount));
    }
}
