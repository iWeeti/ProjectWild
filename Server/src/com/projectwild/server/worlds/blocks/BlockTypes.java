package com.projectwild.server.worlds.blocks;

import com.projectwild.server.worlds.blocks.types.EntranceBlock;
import com.projectwild.server.worlds.blocks.types.StaticBlock;
import com.projectwild.server.worlds.blocks.types.UnbreakableBlock;

public enum BlockTypes {

    STATIC(0, StaticBlock.class),
    ENTRANCE(1, EntranceBlock.class),
    UNBREAKABLE(2, UnbreakableBlock.class);
    
    private int id;
    private Class<? extends Block> blockClass;

    BlockTypes(int id, Class<? extends Block> blockClass) {
        this.id = id;
        this.blockClass = blockClass;
    }
    
    public int getId() {
        return id;
    }
    
    public Class<? extends Block> getBlockClass() {
        return blockClass;
    }
    
    public static Class<? extends Block> getBlockClass(int id) {
        for(BlockTypes type : BlockTypes.values()) {
            if(type.getId() == id)
                return type.getBlockClass();
        }
        return STATIC.getBlockClass();
    }
    
    public static int getBlockId(Class<? extends Block> blockClass) {
        for(BlockTypes type : BlockTypes.values()) {
            if(type.getBlockClass() == blockClass)
                return type.getId();
        }
        return STATIC.getId();
    }
    
}
