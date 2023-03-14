package agent;

import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.AgentTaskDTO;
import logic.DecipherLogic;
import machine.Machine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AgentTask implements Runnable {

    private final Machine enigmaMachine;
    private final Map<String,List<Character>> decryptedMessagesCandidates = new HashMap<>();
    private final AgentTaskDTO details;

    public AgentTask(Machine machine , AgentTaskDTO details) {
        this.details = details;
        enigmaMachine = machine;
    }

    @Override
    public void run() {

        long taskDuration = System.nanoTime();
        StringBuilder decryptedMessage = new StringBuilder();
        Set<String> decryptedWords;

        for (List<Character> initialPosition : details.getInitialPositions()) {

            initializeConfigurationInMachine(initialPosition);
            for (char c: details.getMessageToDecrypt().toCharArray()) {
                decryptedMessage.append(enigmaMachine.encryption(c));
            }

            decryptedWords = DecipherLogic.stringToWords(decryptedMessage.toString().toLowerCase());

            if(decryptedWords.stream().allMatch(details.getDictionary()::contains)){
                decryptedMessagesCandidates.put(decryptedMessage.toString(), initialPosition);
            }

            decryptedMessage.delete(0,decryptedMessage.length());
        }

        taskDuration = System.nanoTime() - taskDuration;
        if(decryptedMessagesCandidates.size() != 0){
            details.getAnswerConsumer().accept(new AgentAnswerDTO(decryptedMessagesCandidates,Thread.currentThread().getName(),taskDuration ));
        }

        details.getTasksMade().update();
    }

    private void initializeConfigurationInMachine(List<Character> initialPosition) {

        int i = 0;
        for (Character pos : initialPosition) {
            enigmaMachine.setInitPositionForRotor(i, pos);
            i++;
        }
    }
}
