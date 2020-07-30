package com.projectwild.server.worlds.commands;

import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.clients.Rank;
import com.projectwild.server.worlds.World;
import com.projectwild.shared.packets.ChatMessagePacket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorldCommand implements Command {

    @Override
    public void execute(Client client, World world, Object[] args) {
        StringBuilder worldInfo = new StringBuilder();
        String sql = "SELECT username FROM Users WHERE id = ? COLLATE NOCASE";

        ResultSet rs = WildServer.getDatabaseController().query(sql, world.getOwner());
        try {
            if(rs.isClosed()) {
                client.sendTCP(new ChatMessagePacket("Something went wrong."));
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            worldInfo.append(String.format("%s ownded by %s\n", world.getName(), rs.getString("username")));
        } catch (SQLException throwables) {
            worldInfo.append(world.getName());
            throwables.printStackTrace();
        }
        worldInfo.append(String.format("POS: [%s, %s] %s blocks tall and %s blocks wide\n", Math.floor(client.getPlayer().getPosition().getX()/32), Math.floor(client.getPlayer().getPosition().getY()/32), world.getHeight(), world.getWidth()));

        StringBuilder trusted = new StringBuilder();
        for (int id : world.getTrusted()){
            String _sql = "SELECT username FROM Users WHERE id = ? COLLATE NOCASE";

            ResultSet resultSet = WildServer.getDatabaseController().query(_sql, id);
            try {
                if(resultSet.isClosed()) {
                    client.sendTCP(new ChatMessagePacket("Something went wrong."));
                    return;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                trusted.append(resultSet.getString("username") + ", ");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (!trusted.toString().isBlank() && !trusted.toString().isEmpty())
            worldInfo.append(String.format("Trusted %s", trusted.toString().substring(0, trusted.toString().length() - 2)));

        client.sendTCP(new ChatMessagePacket(worldInfo.toString()));
    }

    @Override
    public String help() {
        return "Info about the world.";
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
        return new CommandHandler.ArgType[0];
    }
}
