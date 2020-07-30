package com.projectwild.shared.packets.player.local;

public class UpdateHealthPacket {

    private int health;

    public UpdateHealthPacket() {}

    public UpdateHealthPacket(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

}
