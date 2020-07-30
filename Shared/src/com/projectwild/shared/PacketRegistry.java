package com.projectwild.shared;

import com.esotericsoftware.kryo.Kryo;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.LoginDataPacket;
import com.projectwild.shared.packets.LoginResponsePacket;
import com.projectwild.shared.packets.clothing.EquipPacket;
import com.projectwild.shared.packets.clothing.UpdateEquippedPacket;
import com.projectwild.shared.packets.items.ChangeInventoryItemPacket;
import com.projectwild.shared.packets.items.LoadInventoryPacket;
import com.projectwild.shared.packets.items.MoveItemSlotsPacket;
import com.projectwild.shared.packets.player.*;
import com.projectwild.shared.packets.player.local.*;
import com.projectwild.shared.packets.world.*;
import com.projectwild.shared.utils.Vector2;

public class PacketRegistry {
    
    public static void register(Kryo kryo) {
        // Arrays
        kryo.register(byte[].class);
        kryo.register(byte[][].class);
        kryo.register(byte[][][].class);
        kryo.register(Object[].class);

        // Custom Classes
        kryo.register(Vector2.class);
        kryo.register(ItemStack.class);
        kryo.register(ItemStack[].class);
        kryo.register(ItemPreset.class);

        // Packets
        kryo.register(LoginDataPacket.class);
        kryo.register(LoginResponsePacket.class);
        kryo.register(RequestWorldPacket.class);
        kryo.register(WorldDataPacket.class);
        kryo.register(PlayerSpawnPacket.class);
        kryo.register(PlayerRemovePacket.class);
        kryo.register(RequestWorldResponsePacket.class);
        kryo.register(UpdatePositionPacket.class);
        kryo.register(UpdateSpeedMultiplierPacket.class);
        kryo.register(MovePacket.class);
        kryo.register(PlayerAnimationPacket.class);
        kryo.register(InteractBlockPacket.class);
        kryo.register(UpdateBlockPacket.class);
        kryo.register(LoadInventoryPacket.class);
        kryo.register(ChangeInventoryItemPacket.class);
        kryo.register(ChatMessagePacket.class);
        kryo.register(LeaveWorldPacket.class);
        kryo.register(LeaveWorldPacket.class);
        kryo.register(UpdateHasAccessPacket.class);
        kryo.register(UpdateNameTagPacket.class);
        kryo.register(UpdateNetworkedVariablePacket.class);
        kryo.register(CallNetworkedCallbackPacket.class);
        kryo.register(MoveItemSlotsPacket.class);
        kryo.register(UpdateNoclipPacket.class);
        kryo.register(UpdateHealthPacket.class);
        kryo.register(UpdateEquippedPacket.class);
        kryo.register(EquipPacket.class);

    }
    
}
