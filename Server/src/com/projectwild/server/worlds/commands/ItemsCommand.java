package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.packets.ChatMessagePacket;

public class ItemsCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(!CommandHandler.isMod(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Admin Only");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for(ItemPreset preset : ItemPreset.getItemPresets()) {
            builder.append(preset.getId());
            builder.append(" : ");
            builder.append(preset.getName());
            builder.append("\n");
        }

        ChatMessagePacket chatMessagePacket = new ChatMessagePacket(builder.toString());
        WildServer.getServer().sendToTCP(client.getSocket(), chatMessagePacket);
    }

    @Override
    public String help() {
        return "Shows All Items & Ids";
    }

}
