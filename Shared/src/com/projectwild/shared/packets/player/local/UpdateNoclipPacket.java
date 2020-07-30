package com.projectwild.shared.packets.player.local;

public class UpdateNoclipPacket {

    private boolean active;

    public UpdateNoclipPacket() {}

    public UpdateNoclipPacket(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

}
