package com.projectwild.shared.packets.player.local;

public class UpdateSpeedMultiplierPacket {

    private float speedMultiplier;

    public UpdateSpeedMultiplierPacket() {}

    public UpdateSpeedMultiplierPacket(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

}
