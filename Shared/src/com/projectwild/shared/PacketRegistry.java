package com.projectwild.shared;

import com.esotericsoftware.kryo.Kryo;
import com.projectwild.shared.packets.LoginDataPacket;
import com.projectwild.shared.packets.LoginResponsePacket;
import com.projectwild.shared.packets.player.PlayerAnimationPacket;
import com.projectwild.shared.packets.player.PlayerRemovePacket;
import com.projectwild.shared.packets.player.PlayerSpawnPacket;
import com.projectwild.shared.packets.player.UpdatePositionPacket;
import com.projectwild.shared.packets.player.local.MovePacket;
import com.projectwild.shared.packets.player.local.UpdatePlayerAttributesPacket;
import com.projectwild.shared.packets.world.*;
import com.projectwild.shared.utils.Vector2;

public class PacketRegistry {
    
    public static void register(Kryo kryo) {
        // Arrays
        kryo.register(byte[].class);
        kryo.register(byte[][].class);
        kryo.register(byte[][][].class);
        kryo.register(Vector2.class);
        
        // Packets
        kryo.register(LoginDataPacket.class);
        kryo.register(LoginResponsePacket.class);
        kryo.register(RequestWorldPacket.class);
        kryo.register(WorldDataPacket.class);
        kryo.register(PlayerSpawnPacket.class);
        kryo.register(PlayerRemovePacket.class);
        kryo.register(RequestWorldResponsePacket.class);
        kryo.register(UpdatePositionPacket.class);
        kryo.register(UpdatePlayerAttributesPacket.class);
        kryo.register(MovePacket.class);
        kryo.register(PlayerAnimationPacket.class);
        kryo.register(InteractBlockPacket.class);
        kryo.register(UpdateBlockPacket.class);

    }
    
}
