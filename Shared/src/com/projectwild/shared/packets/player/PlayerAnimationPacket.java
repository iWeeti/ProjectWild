package com.projectwild.shared.packets.player;

public class PlayerAnimationPacket {

    private int userId;
    private int animationId;

    public PlayerAnimationPacket() {}

    public PlayerAnimationPacket(int userId, int animationId) {
        this.userId = userId;
        this.animationId = animationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getAnimationId() {
        return animationId;
    }

}
