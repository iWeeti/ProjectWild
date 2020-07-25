package com.projectwild.shared.packets.player.local;

public class UpdatePlayerAttributesPacket {

    private float speedMultiplier;

    public UpdatePlayerAttributesPacket() {}

    public UpdatePlayerAttributesPacket(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

}
