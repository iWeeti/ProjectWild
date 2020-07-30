package com.projectwild.server.worlds.blocks.types;

import com.projectwild.server.worlds.World;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.BlockPreset;

public class HurtingBlock extends Block {

    public HurtingBlock(BlockPreset preset, World world, int x, int y, int z) {
        super(preset, world, x, y, z);
    }

    @Override
    public void deserialize(byte[] data) {

    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public void collision(Player player) {
        player.changeHealth(-2);
    }

    @Override
    public void update() {

    }
}
