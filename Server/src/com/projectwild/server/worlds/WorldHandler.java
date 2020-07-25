package com.projectwild.server.worlds;

import java.util.concurrent.CopyOnWriteArrayList;

public class WorldHandler {

    private CopyOnWriteArrayList<World> worlds;
    
    public WorldHandler() {
        worlds = new CopyOnWriteArrayList<>();
    }
    
    public World getWorld(String worldName) {
        for(World world : worlds) {
            if(world.getName().toLowerCase().equals(worldName.toLowerCase()))
                return world;
        }
        World world = new World(worldName);
        worlds.add(world);
        return world;
    }

}
