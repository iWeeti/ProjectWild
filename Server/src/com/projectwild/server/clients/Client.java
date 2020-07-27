package com.projectwild.server.clients;

import com.projectwild.server.WildServer;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.packets.items.ChangeInventoryItemPacket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    private int userId, socket;
    private String username;

    private Player player;
    private ItemStack[] inventory;
    
    public Client(int socket, int userId, String username) {
        this.socket = socket;
        this.userId = userId;
        this.username = username;

        // Loading The Inventory
        String sql = "SELECT slot, itemId, amount FROM Items WHERE userId = ?";
        ResultSet rs = WildServer.getDatabaseController().query(sql, userId);
        inventory = new ItemStack[48];
        try {
            while(rs.next()) {
                ItemStack stack = new ItemStack(ItemPreset.getPreset(rs.getInt("itemId")), rs.getInt("amount"));
                inventory[rs.getInt("slot")] = stack;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean changeItems(ItemPreset preset, int amount) {
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null)
                continue;

            if(inventory[i].getItemPreset().getId() == preset.getId()) {
                int newAmount = inventory[i].getAmount() + amount;
                if(newAmount > 999)
                    return false;

                if(newAmount != 0) {
                    inventory[i] = new ItemStack(preset, newAmount);
                    String sql = "UPDATE Items SET amount = ? WHERE userId = ? AND itemId = ?";
                    WildServer.getDatabaseController().update(sql, newAmount, userId, preset.getId());
                } else {
                    inventory[i] = null;
                    String sql = "DELETE FROM Items WHERE userId = ? AND itemId = ?";
                    WildServer.getDatabaseController().delete(sql, userId, preset.getId());
                }

                ChangeInventoryItemPacket packet = new ChangeInventoryItemPacket(i, inventory[i]);
                WildServer.getServer().sendToTCP(socket, packet);
                return true;
            }
        }

        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null) {
                inventory[i] = new ItemStack(preset, amount);
                String sql = "INSERT INTO Items (userId, slot, itemId, amount) VALUES (?, ?, ?, ?)";
                WildServer.getDatabaseController().insert(sql, userId, i, preset.getId(), amount);

                ChangeInventoryItemPacket packet = new ChangeInventoryItemPacket(i, inventory[i]);
                WildServer.getServer().sendToTCP(socket, packet);
                return true;
            }
        }
        return false;
    }

    public ItemStack getInventorySlot(int slot) {
        if(slot > inventory.length)
            return null;
        return inventory[slot];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSocket() {
        return socket;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

}
