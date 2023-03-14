package decryptionDtos;

import logic.Counter;
import machineDtos.EngineComponentsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AgentTaskDTO {

    private List<Integer> rotorsId;
    private int numOfUsedRotors;
    private String reflectorId;
    private List<List<Character>> initialPositions = new ArrayList<>();
    private String messageToDecrypt;
    private Set<String> dictionary;
    private EngineComponentsDTO engineComponentsDTO;
    private Consumer<AgentAnswerDTO> answerConsumer;
    private Counter tasksMade;

    public AgentTaskDTO(){}

    public AgentTaskDTO(AgentTaskDTO details) {
        rotorsId = new ArrayList<>(details.rotorsId);
        numOfUsedRotors = details.numOfUsedRotors;
        reflectorId = details.reflectorId;
        messageToDecrypt = details.messageToDecrypt;
        dictionary = details.dictionary;
        answerConsumer = details.answerConsumer;
        tasksMade = details.tasksMade;
        engineComponentsDTO = details.engineComponentsDTO;
        for(List<Character> list:details.initialPositions){
            initialPositions.add(new ArrayList<>(list));
        }
    }


    public List<List<Character>> getInitialPositions() {
        return initialPositions;
    }

    public void setInitialPositions(List<List<Character>> initialPositions) {
        this.initialPositions = initialPositions;
    }

    public String getMessageToDecrypt() {
        return messageToDecrypt;
    }

    public void setMessageToDecrypt(String messageToDecrypt) {
        this.messageToDecrypt = messageToDecrypt;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
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

    public EngineComponentsDTO getEngineComponentsDTO() {
        return engineComponentsDTO;
    }

    public void setEngineComponentsDTO(EngineComponentsDTO engineComponentsDTO) {
        this.engineComponentsDTO = engineComponentsDTO;
    }

    public int getNumOfUsedRotors() {
        return numOfUsedRotors;
    }

    public void setNumOfUsedRotors(int numOfUsedRotors) {
        this.numOfUsedRotors = numOfUsedRotors;
    }

    public Consumer<AgentAnswerDTO> getAnswerConsumer() {
        return answerConsumer;
    }

    public void setAnswerConsumer(Consumer<AgentAnswerDTO> answerConsumer) {
        this.answerConsumer = answerConsumer;
    }

    public void setTasksMade(Counter tasksMade) {
        this.tasksMade = tasksMade;
    }

    public Counter getTasksMade() {
        return tasksMade;
    }
}
