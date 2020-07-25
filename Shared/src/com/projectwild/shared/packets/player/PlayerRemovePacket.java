package com.projectwild.shared.packets.player;

public class PlayerRemovePacket {

    private int userId;

    public PlayerRemovePacket() {}

    public PlayerRemovePacket(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

}
