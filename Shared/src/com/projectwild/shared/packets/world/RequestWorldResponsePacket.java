package com.projectwild.shared.packets.world;

public class RequestWorldResponsePacket {

    private boolean success;
    private String message;

    public RequestWorldResponsePacket() {}

    public RequestWorldResponsePacket(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
