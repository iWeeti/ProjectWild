package com.projectwild.shared.packets.player;

public class UpdateNameTagPacket {

    private int userId;
    private String nametag;

    public UpdateNameTagPacket() {}

    public UpdateNameTagPacket(int userId, String nametag) {
        this.userId = userId;
        this.nametag = nametag;
    }

    public int getUserId() {
        return userId;
    }

    public String getNametag() {
        return nametag;
    }

}
