package org.openhab.binding.zwavejs.model;

public abstract class Command {
    public String getCommand() {
        return getClass().getCanonicalName().substring(42)
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

    private String messageId;

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
