package com.projectwild.shared.packets.player;

import com.projectwild.shared.utils.Vector2;

public class PlayerSpawnPacket {

    private int userId;
    private String username;
    private Vector2 position;

    private boolean isLocal;

    public PlayerSpawnPacket() {}

    public PlayerSpawnPacket(int userId, String username, Vector2 position, boolean isLocal) {
        this.userId = userId;
        this.username = username;
        this.position = position.copy();
        this.isLocal = isLocal;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isLocal() {
        return isLocal;
    }

}
