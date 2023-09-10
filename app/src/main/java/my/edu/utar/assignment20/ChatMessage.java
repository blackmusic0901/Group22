package my.edu.utar.assignment20;

public class ChatMessage {
    private String message;
    private boolean isUserMessage;
    private String text; // The message text
    private boolean sentByUser; // true if sent by the user, false otherwise

    public ChatMessage(String text, boolean sentByUser) {
        this.text = text;
        this.sentByUser = sentByUser;
        this.isUserMessage = sentByUser;
    }

    public String getText() {
        return text;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }

    public boolean isSentByUser() {
        return sentByUser;
    }

}
