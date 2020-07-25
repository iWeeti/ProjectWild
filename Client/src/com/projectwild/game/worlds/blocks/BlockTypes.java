package com.projectwild.game.worlds.blocks;

import com.projectwild.game.worlds.blocks.types.StaticBlock;

public enum BlockTypes {
    
    STATIC(0, StaticBlock.class);
    
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

