package com.projectwild.shared.packets.player;

import com.projectwild.shared.utils.Vector2;

public class UpdatePositionPacket {

    private int userId;
    private Vector2 position;

    public UpdatePositionPacket() {}

    public UpdatePositionPacket(int userId, Vector2 position) {
        this.userId = userId;
        this.position = position.copy();
    }

    public int getUserId() {
        return userId;
    }

    public Vector2 getPosition() {
        return position;
    }

}
