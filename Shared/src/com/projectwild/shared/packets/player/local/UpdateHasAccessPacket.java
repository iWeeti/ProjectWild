package com.projectwild.shared.packets.player.local;

public class UpdateHasAccessPacket {

    private boolean hasAccess;

    public UpdateHasAccessPacket() {}

    public UpdateHasAccessPacket(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

}
