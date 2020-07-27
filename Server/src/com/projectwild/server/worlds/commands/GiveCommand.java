package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.packets.ChatMessagePacket;

public class GiveCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(!CommandHandler.isAdmin(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Admin Only");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(args.length < 2) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Missing Arguments");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        try {
            int itemId = Integer.parseInt(args[0]);
            if(itemId >= ItemPreset.getItemPresets().length) {
                ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Invalid ItemID");
                WildServer.getServer().sendToTCP(client.getSocket(), packet);
                return;
            }

            ItemPreset itemPreset = ItemPreset.getPreset(itemId);
            int amount = Integer.parseInt(args[1]);

            if(args.length == 3) {
                Client c = WildServer.getClientHandler().getClientByUsername(args[2]);
                if(c == null) {
                    ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Couldn't Find Specified Player");
                    WildServer.getServer().sendToTCP(client.getSocket(), packet);
                    return;
                }
                c.changeItems(itemPreset, amount);
            } else {
                client.changeItems(itemPreset, amount);
            }
        } catch(NumberFormatException e) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Please Enter A Valid ItemId & Amount");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }
    }

    @Override
    public String help() {
        return "Gives Specified Item(s)";
    }

}
