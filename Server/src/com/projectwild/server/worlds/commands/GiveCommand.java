package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.ItemPreset;

public class GiveCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {

        ItemPreset itemPreset = null;
        for(ItemPreset preset : ItemPreset.getItemPresets()) {
            if(preset.getName().toLowerCase().equals(((String) args[0]).toLowerCase()))
                itemPreset = preset;
        }

        if(itemPreset == null) {
            client.sendChatMessage("[RED]Failed![WHITE] Please Enter A Valid Item Name");
            return;
        }


        int amount = (int) args[1];

        client.changeItems(itemPreset, amount);

        client.sendChatMessage(String.format("[GREEN]Success![WHITE] Added x%s %s", amount, itemPreset.getName()));
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
        return Rank.USER;
    }

    @Override
    public CommandHandler.ArgType[] arguments() {
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.STRING, CommandHandler.ArgType.INTEGER};
    }

}
