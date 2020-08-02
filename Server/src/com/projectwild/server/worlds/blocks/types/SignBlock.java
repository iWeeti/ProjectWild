package com.projectwild.server.worlds.blocks.types;

import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.BlockPreset;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SignBlock extends Block {

    public SignBlock(BlockPreset preset, World world, int x, int y, int z) {
        super(preset, world, x, y, z);
    }

    @Override
    public void deserialize(byte[] data) {
        setText(new String(data));
    }

    @Override
    public byte[] serialize() {
        return getNWString("text").getBytes();
    }

    @Override
    public void collision(Player ply) {

    }

    @Override
    public void update() {

    }

    public void setText(String text){
        setNWString("text", text);
    }
}
