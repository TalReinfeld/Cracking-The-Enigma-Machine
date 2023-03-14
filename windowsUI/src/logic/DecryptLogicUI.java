package logic;

import components.body.details.DecryptionManagerController;
import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.DecryptionArgumentsDTO;
import logic.tasks.DecryptMessageTask;
import manager.DecryptionManager;

import java.util.Set;
import java.util.function.Consumer;

public class DecryptLogicUI {

    private DecryptMessageTask currentTask;
    private DecryptionManagerController controllerToUpdate;
    private DecryptionManager decryptionManager;

    public void setDecryptionManager(DecryptionManager decryptionManager) {
        this.decryptionManager = decryptionManager;
    }

    public void setControllerToUpdate(DecryptionManagerController controllerToUpdate) {
        this.controllerToUpdate = controllerToUpdate;
    }

    public void decryptMessage(Consumer<AgentAnswerDTO> updateCandidates, DecryptionArgumentsDTO args){

        currentTask = new DecryptMessageTask(decryptionManager,args, updateCandidates);
        controllerToUpdate.bindTaskToController(currentTask);

        new Thread(currentTask).start();
    }

    public void cancelTask(){
        currentTask.cancel();
    }

    public int getMaxAgentTask(){
        return decryptionManager.getNumberOfAgents();
    }

    public double getTaskSize(){
        return decryptionManager.getTaskAmount();
    }

    public String getStringWithoutSpecialChars(String message){

        return DecipherLogic.excludeSpecialCharactersFromWord(message, decryptionManager.getExcludeChars());
    }

    public Boolean isWordsInTheDictionary(String Message){

        Set<String> words = DecipherLogic.stringToWords(Message.toLowerCase());

        return decryptionManager.getWordsDictionary().containsAll(words);
    }
}
