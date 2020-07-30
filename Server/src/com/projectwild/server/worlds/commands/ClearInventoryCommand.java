package com.projectwild.server.worlds.commands;

import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.ItemStack;

public class ClearInventoryCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        Client c = (Client) args[0];
        for(ItemStack stack : c.getInventory()) {
            if(stack != null)
                c.changeItems(stack.getItemPreset(), -1 * stack.getAmount());
        }
    }

    @Override
    public String help() {
        return "Clears Players Inventory";
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
        return new CommandHandler.ArgType[] {CommandHandler.ArgType.CLIENT};
    }

}
