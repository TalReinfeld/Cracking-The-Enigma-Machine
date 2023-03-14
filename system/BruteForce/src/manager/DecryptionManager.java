package manager;

import decryptionDtos.AgentAnswerDTO;
import decryptionDtos.AgentTaskDTO;
import logic.*;
import machineDtos.EngineDTO;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DecryptionManager {

    private static int maxNumberOfAgents;
    private static Set<String> wordsDictionary;
    private static String excludeChars;
    private EnigmaSystemEngine machineEngine;
    private ThreadPoolExecutor threadPool;
    private BlockingQueue<Runnable> agentTasks;
    private final BlockingQueue<AgentAnswerDTO> answersQueue = new LinkedBlockingQueue<>();
    private final Consumer<AgentAnswerDTO> updateQueueConsumer;
    double tasksAmount;

    public DecryptionManager(EnigmaSystemEngine engine){

        agentTasks = new LinkedBlockingQueue<>(DecipherLogic.MAXIMUM_TASKS);
        threadPool = new ThreadPoolExecutor(maxNumberOfAgents, maxNumberOfAgents,5, TimeUnit.SECONDS,agentTasks,createThreadFactory());
        updateQueueConsumer = e -> {
            try {
                answersQueue.put(e);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        };
        machineEngine = engine;
        EngineDTO details = engine.displayingMachineSpecification();
        tasksAmount = Math.pow(details.getEngineComponentsInfo().getABC().length(), details.getNumOfUsedRotors());
    }

    public double getTaskAmount(){

        return tasksAmount;
    }
    public static void setMaxNumberOfAgents(int maxNumberOfAgents) {

        DecryptionManager.maxNumberOfAgents = maxNumberOfAgents;
    }

    public static void setWordsDictionary(Set<String> wordsDictionary) {

        DecryptionManager.wordsDictionary = wordsDictionary;
    }

    public static void setExcludeChars(String excludeChars) {

        DecryptionManager.excludeChars = excludeChars;
    }

    public String getExcludeChars(){

        return excludeChars;
    }

    public BlockingQueue<AgentAnswerDTO> getAnswersQueue(){

        return answersQueue;
    }

    public int getNumberOfAgents() {

        return maxNumberOfAgents;
    }

    public Set<String> getWordsDictionary() {

        return wordsDictionary;
    }

    public void decryptMessage(int taskSize, DifficultyLevel level, String message, List<Integer> rotorsId, String reflectorId, Counter tasksMade, int agentsNumber){

        AgentTaskDTO details = new AgentTaskDTO();
        EngineDTO engineDetails = machineEngine.displayingMachineSpecification();
        details.setNumOfUsedRotors(engineDetails.getNumOfUsedRotors());
        details.setDictionary(wordsDictionary);
        details.setMessageToDecrypt(message);
        details.setRotorsId(rotorsId);
        details.setReflectorId(reflectorId);
        details.setTasksMade(tasksMade);

        if(machineEngine instanceof EnigmaEngine) {
            details.setEngineComponentsDTO(((EnigmaEngine)machineEngine).getEngineComponentsDto());
        }

        answersQueue.clear();
        details.setAnswerConsumer(updateQueueConsumer);
        threadPool.setCorePoolSize(agentsNumber);
        threadPool.setMaximumPoolSize(agentsNumber);
        threadPool.prestartAllCoreThreads();
        new Thread(new TasksProducer(details,taskSize,agentTasks,level)).start();
    }

    public void shoutDown(){

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(5,TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ThreadFactory createThreadFactory(){

        return new ThreadFactory() {
            final String name = "Agent";
            int agentNumber = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,name + agentNumber++);
            }
        };
    }
}
