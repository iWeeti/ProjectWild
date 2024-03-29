package com.projectwild.server.worlds;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.projectwild.server.WildServer;
import com.projectwild.server.clients.Client;
import com.projectwild.server.worlds.blocks.Block;
import com.projectwild.server.worlds.players.Player;
import com.projectwild.shared.packets.ChatMessagePacket;
import com.projectwild.shared.packets.PlaySoundPacket;
import com.projectwild.shared.packets.clothing.UpdateEquippedPacket;
import com.projectwild.shared.packets.items.LoadInventoryPacket;
import com.projectwild.shared.packets.world.*;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldListener extends Listener {
    
    @Override
    public void received(Connection connection, Object obj) {
        if(obj instanceof RequestWorldPacket) {
            RequestWorldPacket packet = (RequestWorldPacket) obj;
    
            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());
            if(client == null)
                return;

            if(packet.getWorld().equals("") || packet.getWorld() == null) {
                connection.sendTCP(new RequestWorldResponsePacket(false, "Please Enter A World Name"));
                return;
            }

            // Make World Name Only Alphanumeric Characters
            String regex = "^[a-zA-Z0-9]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(packet.getWorld());
            if(!matcher.matches()) {
                connection.sendTCP(new RequestWorldResponsePacket(false, "World Names Can Only Include Alphanumeric Characters"));
                return;
            }

            if(packet.getWorld().length() > 10) {
                connection.sendTCP(new RequestWorldResponsePacket(false, "World Names Can Only Be 10 Characters Long"));
                return;
            }

            connection.sendTCP(new RequestWorldResponsePacket(true, null));
            
            World world = WildServer.getWorldHandler().getWorld(packet.getWorld());
            
            Block[][][] blocks = world.getBlocks();
            ByteBuffer buffer = ByteBuffer.allocate(0);
            for(int y = 0; y < blocks.length; y++) {
                for(int x = 0; x < blocks[y].length; x++) {
                    for(int z = 0; z < 2; z++) {
                        Block block = blocks[y][x][z];
                        byte[] data = block.serializeNetworkVariables();
                        buffer = ByteBuffer.allocate(buffer.capacity() + 8 + data.length).put(buffer.array()).putInt(block.getBlockPreset().getId()).putInt(data.length).put(data);
                    }
                }
            }
            connection.sendTCP(new WorldDataPacket(world.getName(), blocks[0].length, blocks.length, buffer.array(), world.getBackground()));

            client.setPlayer(world.createPlayer(client));

            connection.sendTCP(new LoadInventoryPacket(client.getInventory()));

            world.playSoundForAll("whoosh_1");
        }

        if(obj instanceof LeaveWorldPacket) {
            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());
            if(client == null)
                return;

            client.getPlayer().getWorld().destroyPlayer(client.getPlayer());
        }

        if(obj instanceof CallNetworkedCallbackPacket) {
            CallNetworkedCallbackPacket packet = (CallNetworkedCallbackPacket) obj;

            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());
            if(client == null)
                return;

            if(client.getPlayer() == null)
                return;

            World world = client.getPlayer().getWorld();
            Block.Callback callback = world.getBlocks()[packet.getY()][packet.getX()][packet.getZ()].getNWCallback(packet.getCallback());
            if(callback != null)
                callback.callback(client, packet.getData());
        }

        if(obj instanceof ChatMessagePacket) {
            ChatMessagePacket packet = (ChatMessagePacket) obj;

            Client client = WildServer.getClientHandler().getClientBySocket(connection.getID());
            if(client == null)
                return;

            if(client.getPlayer() == null)
                return;

            // Make World Name Only Alphanumeric Characters
            String regex = "^[a-zA-Z0-9öäåÖÄÅ/!?#$%^&*()_\\-{}|'\\[\\].,\b\\s\":;]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(packet.getMessage());
            if(!matcher.matches()) {
                client.sendChatMessage("[RED]FAILED! [WHITE] What Are You Tryina Do -,-");
                return;
            }

            if(packet.getMessage().startsWith("/")) {
                String message = packet.getMessage();
                while(message.contains("\"")) {
                    int start = message.indexOf("\"");
                    message = message.replaceFirst("\"", "");
                    int end = message.indexOf("\"");
                    if(end == -1)
                        break;
                    message = message.replaceFirst("\"", "");

                    String arg = message.substring(start, end-2);
                    String modifiedArg = arg.replaceAll(" ", "@");
                    message = message.replace(arg, modifiedArg);
                }
                String[] arr = message.split(" ");
                String command = arr[0].substring(1);
                String[] args = Arrays.copyOfRange(arr, 1, arr.length);
                for(int i = 0; i < args.length; i++)
                    args[i] = args[i].replaceAll("@", " ");
                WildServer.getCommandHandler().executeCommand(command, client, args);
                return;
            }

            if(packet.getMessage().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "").length() == 0)
                return;


            World world = client.getPlayer().getWorld();

            ChatMessagePacket chatMessagePacket = new ChatMessagePacket(String.format("[YELLOW]%s >> [WHITE]%s", client.getUsername(), packet.getMessage()));

            for(Player ply : world.getPlayers()) {
                ply.getClient().sendTCP(chatMessagePacket);
            }

        }
    }
    
}
