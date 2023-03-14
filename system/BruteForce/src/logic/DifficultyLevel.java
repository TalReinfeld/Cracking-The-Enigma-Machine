package logic;

import agent.AgentTask;
import components.PlugBoard;
import components.Reflector;
import components.Rotor;
import decryptionDtos.AgentTaskDTO;
import machine.Machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public enum DifficultyLevel {

    EASY{
        @Override
        public void initialTasks(AgentTaskDTO details, BlockingQueue<Runnable> tasksQueue) {

            List<Rotor> rotors = new ArrayList<>();

            for (Integer rotorId : details.getRotorsId()){
                details.getEngineComponentsDTO().getOptionalRotors().forEach(
                        rotor -> {
                            if(rotor.getId() == rotorId) {
                                rotors.add(rotor.clone());
                                System.out.println("rotorId: " + rotor.getId());
                            }
                        });
            }


            List<Integer> rotorsPositions = rotors.stream().mapToInt(
                    rotor -> rotor.getPositionOfChar(details.getEngineComponentsDTO().getABC().charAt(0))).boxed().collect(Collectors.toList());

            Reflector reflector = details.getEngineComponentsDTO().getOptionalReflectors().stream().filter(
                    reflector1 -> reflector1.getId().equals(details.getReflectorId())).findFirst().get();

            Machine enigmaMachine = new Machine(rotors, rotorsPositions, reflector , details.getEngineComponentsDTO().getABC(), new PlugBoard());

            try {
                tasksQueue.put(new AgentTask(enigmaMachine,details));
            } catch (InterruptedException e) {
                e.printStackTrace();        // handle here....
            }
        }
    },
    MEDIUM {
        @Override
        public void initialTasks(AgentTaskDTO details, BlockingQueue<Runnable> tasksQueue) {

            for (Reflector reflector : details.getEngineComponentsDTO().getOptionalReflectors()) {
                details.setReflectorId(reflector.getId());
                EASY.initialTasks(details,tasksQueue);
            }
        }
    },
    HARD{
        @Override
        public void initialTasks(AgentTaskDTO details, BlockingQueue<Runnable> tasksQueue) {

           Permutations permutations = new Permutations(details.getRotorsId().stream().mapToInt(i->(int)i).toArray());

            for (List<Integer> permutation:permutations) {

                details.setRotorsId(permutation);
                MEDIUM.initialTasks(details,tasksQueue);

            }
        }
    },
    IMPOSSIBLE{
        @Override
        public void initialTasks(AgentTaskDTO details, BlockingQueue<Runnable> tasksQueue) {

            BinomialCoefficient binomialCoefficient = new BinomialCoefficient(
                    details.getEngineComponentsDTO().getOptionalRotors().size(),details.getNumOfUsedRotors());

            for (int[] combination : binomialCoefficient.getCombinations()) {
                List<Integer> rotorsId = Arrays.stream(combination).boxed().collect(Collectors.toList());
                details.setRotorsId(rotorsId);
                HARD.initialTasks(details,tasksQueue);
            }
        }
    };

    abstract public void initialTasks(AgentTaskDTO details, BlockingQueue<Runnable> tasksQueue);
}
