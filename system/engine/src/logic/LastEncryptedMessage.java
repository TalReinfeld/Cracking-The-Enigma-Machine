package logic;

public class LastEncryptedMessage {

    private String message;
    private String encrypted;
    private long time;

    public LastEncryptedMessage(String message, String encryptedString, long encryptedTime) {
        this.message = message;
        this.encrypted = encryptedString;
        this.time = encryptedTime;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}
