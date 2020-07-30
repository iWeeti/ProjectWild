package com.projectwild.server.clients;

import com.projectwild.server.WildServer;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.ClothingPreset;
import com.projectwild.shared.ItemPreset;
import com.projectwild.shared.ItemStack;
import com.projectwild.shared.ItemTypes;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.clothing.UpdateEquippedPacket;
import com.projectwild.shared.packets.items.ChangeInventoryItemPacket;
import com.projectwild.shared.packets.items.LoadInventoryPacket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    private int userId, socket;
    private String username;

    private Rank rank;

    private Player player;
    private ItemStack[] inventory;
    private ItemStack[] equipped;

    public Client(int socket, int userId) {
        this.socket = socket;
        this.userId = userId;

        // Loading Client Data
        int inventorySpace = 44;
        {
            String sql = "SELECT username, rank, inventorySpace FROM Users WHERE id = ? COLLATE NOCASE";
            ResultSet rs = WildServer.getDatabaseController().query(sql, userId);
            try {
                if (rs.next()) {
                    this.username = rs.getString("username");
                    this.rank = Rank.getRank(rs.getString("rank"));
                    inventorySpace = rs.getInt("inventorySpace");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Loading The Inventory
        {
            String sql = "SELECT invType, slot, itemId, amount FROM Items WHERE userId = ?";
            ResultSet rs = WildServer.getDatabaseController().query(sql, userId);
            inventory = new ItemStack[inventorySpace];
            equipped = new ItemStack[9];
            try {
                while (rs.next()) {
                    ItemStack stack = new ItemStack(ItemPreset.getPreset(rs.getInt("itemId")), rs.getInt("amount"));
                    switch(rs.getString("invType")) {
                        case "main":
                            inventory[rs.getInt("slot")] = stack;
                            break;
                        case "equipped":
                            equipped[rs.getInt("slot")] = stack;
                            break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTCP(Object packet) {
        WildServer.getServer().sendToTCP(socket, packet);
    }

    public void sendUDP(Object packet) {
        WildServer.getServer().sendToUDP(socket, packet);
    }

    public void sendChatMessage(String message) {
        sendTCP(new ChatMessagePacket(message));
    }

    public void sendChatMessage(String message, Object... args) {
        message = String.format(message, args);
        sendTCP(new ChatMessagePacket(message));
    }

    public void setInventorySize(int size) {
        String sql = "UPDATE Users SET inventorySpace = ? WHERE id = ?";
        WildServer.getDatabaseController().update(sql, size, userId);

        ItemStack[] tempInventory = new ItemStack[size];
        for(int i = 0; i < Math.min(tempInventory.length, inventory.length); i++) {
            tempInventory[i] = inventory[i];
        }
        inventory = tempInventory;

        LoadInventoryPacket packet = new LoadInventoryPacket(inventory);
        sendTCP(packet);
    }

    public boolean changeItems(ItemPreset preset, int amount) {
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null)
                continue;

            if(inventory[i].getItemPreset().getId() == preset.getId()) {
                int newAmount = inventory[i].getAmount() + amount;
                if(newAmount > inventory[i].getItemPreset().getMaxStack())
                    continue;

                if(newAmount != 0) {
                    inventory[i] = new ItemStack(preset, newAmount);
                    String sql = "UPDATE Items SET amount = ? WHERE userId = ? AND itemId = ? AND invType = ?";
                    WildServer.getDatabaseController().update(sql, newAmount, userId, preset.getId(), "main");
                } else {
                    inventory[i] = null;
                    String sql = "DELETE FROM Items WHERE userId = ? AND itemId = ? invType = ?";
                    WildServer.getDatabaseController().delete(sql, userId, preset.getId(), "main");
                }

                sendTCP(new ChangeInventoryItemPacket(i, inventory[i]));
                return true;
            }
        }

        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null) {
                inventory[i] = new ItemStack(preset, amount);
                String sql = "INSERT INTO Items (userId, invType, slot, itemId, amount) VALUES (?, ?, ?, ?, ?)";
                WildServer.getDatabaseController().insert(sql, userId, "main", i, preset.getId(), amount);

                sendTCP(new ChangeInventoryItemPacket(i, inventory[i]));
                return true;
            }
        }
        return false;
    }

    public void setInventorySlot(int slot, ItemStack stack) {
        if(slot < 0 || slot >= inventory.length)
            return;

        if(stack == null || stack.getAmount() <= 0) {
            inventory[slot] = null;
            String sql = "DELETE FROM Items WHERE userId = ? AND slot = ? AND invType = ?";
            WildServer.getDatabaseController().delete(sql, userId, slot, "main");
            sendTCP(new ChangeInventoryItemPacket(slot, null));
            return;
        }

        if(inventory[slot] == null) {
            String sql = "INSERT INTO Items (userId, invType, slot, itemId, amount) VALUES (?, ?, ?, ?, ?)";
            WildServer.getDatabaseController().insert(sql, userId, "main", slot, stack.getItemPreset().getId(), stack.getAmount());
        } else {
            String sql = "UPDATE Items SET amount = ?, itemId = ? WHERE userId = ? AND slot = ? AND invType = ?";
            WildServer.getDatabaseController().update(sql, stack.getAmount(), stack.getItemPreset().getId(), userId, slot, "main");
        }

        inventory[slot] = stack;

        sendTCP(new ChangeInventoryItemPacket(slot, stack));
    }

    public ItemStack getInventorySlot(int slot) {
        if(slot > inventory.length)
            return null;
        return inventory[slot];
    }

    public void equip(int invSlot, int clothingSlot) {
        if(invSlot < 0 || clothingSlot < 0)
            return;

        if(invSlot > inventory.length || clothingSlot > equipped.length)
            return;

        if(inventory[invSlot] == null && equipped[clothingSlot] != null) {
            setInventorySlot(invSlot, new ItemStack(equipped[clothingSlot].getItemPreset(), equipped[clothingSlot].getAmount()));
            equipped[clothingSlot] = null;

            String sql = "DELETE FROM Items WHERE userId = ? AND slot = ? AND invType = ?";
            WildServer.getDatabaseController().delete(sql, userId, clothingSlot, "equipped");
        } else if(inventory[invSlot] != null && equipped[clothingSlot] == null) {
            ItemStack stack = inventory[invSlot];

            if(stack.getItemPreset().getItemType() != ItemTypes.CLOTHING.getId())
                return;

            ClothingPreset preset = ClothingPreset.getPreset(stack.getItemPreset().getClothingId());

            if(preset.getSlot() != clothingSlot)
                return;

            setInventorySlot(invSlot, new ItemStack(stack.getItemPreset(), stack.getAmount()-1));
            equipped[clothingSlot] = new ItemStack(stack.getItemPreset(), 1);

            String sql = "INSERT INTO Items (userId, invType, slot, itemId, amount) VALUES (?, ?, ?, ?, ?)";
            WildServer.getDatabaseController().insert(sql, userId, "equipped", clothingSlot, stack.getItemPreset().getId(), stack.getAmount());
        } else {
            return;
        }

        if(player == null)
            return;

        // Send The New Clothing To All Clients In World
        UpdateEquippedPacket packet = new UpdateEquippedPacket(userId, equipped);
        for(Player ply : player.getWorld().getPlayers())
            ply.getClient().sendTCP(packet);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setRank(Rank rank) {
        this.rank = rank;

        String sql = "UPDATE Users SET rank = ? WHERE id = ?";
        WildServer.getDatabaseController().update(sql, rank.getIdentifier(), userId);
    }

    public Rank getRank() {
        return rank;
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

    public ItemStack[] getEquipped() {
        return equipped;
    }

}
