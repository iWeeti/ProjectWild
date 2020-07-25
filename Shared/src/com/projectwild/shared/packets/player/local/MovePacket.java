package com.projectwild.shared.packets.player.local;

import com.projectwild.shared.utils.Vector2;

public class MovePacket {

    private Vector2 oldPos, newPos;

    public MovePacket() {}

    public MovePacket(Vector2 oldPos, Vector2 newPos) {
        this.oldPos = oldPos.copy();
        this.newPos = newPos.copy();
    }

    public Vector2 getOldPos() {
        return oldPos;
    }

    public Vector2 getNewPos() {
        return newPos;
    }

}
