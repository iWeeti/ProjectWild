package com.projectwild.server.worlds.blocks.types;

import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.BlockPreset;

public class EntranceBlock extends Block {

    public EntranceBlock(BlockPreset preset, World world, int x, int y, int z) {
        super(preset, world, x, y, z);
        setNWBool("open", false);
    }

    @Override
    public void deserialize(byte[] data) {

    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public void collision(Player ply) {
        if(!getNWBool("open"))
            setNWBool("open", true);
    }

    @Override
    public void update() {
        if(getNWBool("open"))
            setNWBool("open", false);
    }

}
