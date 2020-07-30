package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.packets.ChatMessagePacket;

public class GiveCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] _args) {

        //TODO: better stuff ?
        String[] args = ((String) _args[0]).split(" ");

        if(args.length < 2) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Missing Arguments");
            client.sendTCP(packet);
            return;
        }

        ItemPreset itemPreset = null;
        for(ItemPreset preset : ItemPreset.getItemPresets()) {
            if(preset.getName().toLowerCase().equals(args[0].toLowerCase()))
                itemPreset = preset;
        }

        try {
            if(itemPreset == null && !args[0].toLowerCase().equals("all")) {
                int itemId = Integer.parseInt(args[0]);
                if(itemId > 0 && itemId < ItemPreset.getItemPresets().length) {
                    itemPreset = ItemPreset.getPreset(itemId);
                } else {
                    ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Please Enter A Valid Item Name or ItemID");
                    client.sendTCP(packet);
                    return;
                }
            }

            int amount = Integer.parseInt(args[1]);

            if(args[0].toLowerCase().equals("all")) {
                for(ItemPreset preset : ItemPreset.getItemPresets()) {
                    client.changeItems(preset, amount);
                }
                return;
            }

            if(args.length == 3) {
                Client c = WildServer.getClientHandler().getClientByUsername(args[2]);
                if(c == null) {
                    ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Couldn't Find Specified Player");
                    client.sendTCP(packet);
                    return;
                }
                c.changeItems(itemPreset, amount);
            } else {
                client.changeItems(itemPreset, amount);
            }
        } catch(NumberFormatException e) {
            ChatMessagePacket packet = new ChatMessagePacket("[RED]Failed![WHITE] Please Enter A Valid Item Id/Name & Amount");
            client.sendTCP(packet);
            return;
        }
    }

    @Override
    public String help() {
        return "Gives Specified Item(s)";
    }

    @Override
    public boolean worldOwnerOnly() {
        return false;
    }

    @Override
    public Rank rank() {
        return Rank.MOD;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.STRING_CONCAT};
    }

}
