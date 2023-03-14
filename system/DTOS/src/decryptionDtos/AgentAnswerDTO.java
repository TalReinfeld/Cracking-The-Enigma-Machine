package decryptionDtos;

import java.util.List;
import java.util.Map;

public class AgentAnswerDTO {

    private final Map<String, List<Character>> decryptedMessagesCandidates;
    private final String agentId;
    private final double taskDuration;

    public AgentAnswerDTO(Map<String, List<Character>> decryptedMessagesCandidates, String agentId, double taskDuration) {
        this.decryptedMessagesCandidates = decryptedMessagesCandidates;
        this.agentId = agentId;
        this.taskDuration = taskDuration;
    }

    public double getTaskDuration() {
        return taskDuration;
    }

    public String getAgentId() {
        return agentId;
    }

    public Map<String, List<Character>> getDecryptedMessagesCandidates() {
        return decryptedMessagesCandidates;
    }
}
