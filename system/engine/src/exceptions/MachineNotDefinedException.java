package exceptions;

public class MachineNotDefinedException extends Error{

    public MachineNotDefinedException(){
        super("machine.Machine not defined yet, first define initial code configuration");
    }
}
