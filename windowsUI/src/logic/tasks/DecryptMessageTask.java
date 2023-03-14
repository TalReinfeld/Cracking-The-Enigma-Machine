package logic.tasks;

import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.DecryptionArgumentsDTO;
import javafx.concurrent.Task;
import logic.Counter;
import manager.DecryptionManager;

import java.util.function.Consumer;

public class DecryptMessageTask extends Task<Boolean> {

    DecryptionManager decryptionManager;
    DecryptionArgumentsDTO args;
    Consumer<AgentAnswerDTO> answersConsumer;
    private Counter tasksMade = new Counter();

    public DecryptMessageTask(DecryptionManager decryptionManager, DecryptionArgumentsDTO args, Consumer<AgentAnswerDTO> answersConsumer){

        this.answersConsumer = answersConsumer;
        this.decryptionManager = decryptionManager;
        this.args = args;
    }

    @Override
    protected Boolean call() throws Exception {

        decryptionManager.decryptMessage(
                args.getTaskSize(),args.getLevel(),args.getMessageToDecrypt(),args.getRotorsId(),args.getReflectorId(),tasksMade,args.getAgentsNumber());

        Thread updateTask = new Thread(new UpdateAnswersTask(args.getAmountOfTasks(),tasksMade, decryptionManager.getAnswersQueue(),answersConsumer));
        updateTask.start();

        while(tasksMade.get() < args.getAmountOfTasks()){
            updateProgress(tasksMade.get(), args.getAmountOfTasks());
            Thread.sleep(1000);
        }

        updateProgress(tasksMade.get(), args.getAmountOfTasks());
        if(decryptionManager.getAnswersQueue().isEmpty() && updateTask.isAlive()){
            updateTask.interrupt();
            System.out.println("interrupted...");
        }

        return true;
    }
}
