package decryptionDtos;

import logic.DifficultyLevel;

import java.util.List;

public class DecryptionArgumentsDTO {

    private int taskSize;
    private DifficultyLevel level;
    private String messageToDecrypt;
    private List<Integer> rotorsId;
    private String reflectorId;
    private int agentsNumber;
    private int amountOfTasks;

    public int getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public String getMessageToDecrypt() {
        return messageToDecrypt;
    }

    public void setMessageToDecrypt(String messageToDecrypt) {
        this.messageToDecrypt = messageToDecrypt;
    }

    public List<Integer> getRotorsId() {
        return rotorsId;
    }

    public void setRotorsId(List<Integer> rotorsId) {
        this.rotorsId = rotorsId;
    }

    public String getReflectorId() {
        return reflectorId;
    }

    public void setReflectorId(String reflectorId) {
        this.reflectorId = reflectorId;
    }

    public void setAgentsNumber(int agentsNumber) {
        this.agentsNumber = agentsNumber;
    }

    public int getAgentsNumber() {
        return agentsNumber;
    }

    public int getAmountOfTasks() {
        return amountOfTasks;
    }

    public void setAmountOfTasks(int amountOfTasks) {
        this.amountOfTasks = amountOfTasks;
    }
}
