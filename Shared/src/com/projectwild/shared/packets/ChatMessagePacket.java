package com.projectwild.shared.packets;

public class ChatMessagePacket {

    private String message;

    public ChatMessagePacket() {}

    public ChatMessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
