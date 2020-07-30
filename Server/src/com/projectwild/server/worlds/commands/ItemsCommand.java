package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.packets.ChatMessagePacket;

public class ItemsCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        StringBuilder builder = new StringBuilder();
        for(ItemPreset preset : ItemPreset.getItemPresets()) {
            builder.append(preset.getId());
            builder.append(" : ");
            builder.append(preset.getName());
            builder.append("\n");
        }

        ChatMessagePacket chatMessagePacket = new ChatMessagePacket(builder.toString());
        client.sendTCP(chatMessagePacket);
    }

    @Override
    public String help() {
        return "Shows All Items & Ids";
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }

}
