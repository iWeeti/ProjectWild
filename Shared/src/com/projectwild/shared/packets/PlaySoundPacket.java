package com.projectwild.shared.packets;

public class PlaySoundPacket {

    String sound;

    public PlaySoundPacket() {
    }

    public PlaySoundPacket(String sound) {
        this.sound = sound;
    }

    public String getSound() {
        return sound;
    }
}
