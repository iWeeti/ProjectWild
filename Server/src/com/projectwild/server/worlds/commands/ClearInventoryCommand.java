package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.packets.ChatMessagePacket;

public class ClearInventoryCommand implements Command {

    @Override
    public void execute(Client client, String[] args) {
        if(!CommandHandler.isAdmin(client)) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Admin Only");
            WildServer.getServer().sendToTCP(client.getSocket(), packet);
            return;
        }

        if(args.length == 1) {
            Client c = WildServer.getClientHandler().getClientByUsername(args[2]);
            if(c == null) {
                ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Couldn't Find Specified Player");
                WildServer.getServer().sendToTCP(client.getSocket(), packet);
                return;
            }

            for(ItemStack stack : c.getInventory()) {
                if(stack != null)
                    c.changeItems(stack.getItemPreset(), -1 * stack.getAmount());
            }
        } else {
            for(ItemStack stack : client.getInventory()) {
                if(stack != null)
                    client.changeItems(stack.getItemPreset(), -1 * stack.getAmount());
            }
        }
    }

    @Override
    public String help() {
        return "Clears Players Inventory";
    }

}
