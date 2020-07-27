package com.projectwild.shared;

public enum ItemTypes {

    ITEM(0),
    BLOCK(1),
    CLOTHING(2);

    private int id;

    ItemTypes(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ItemTypes getType(int id) {
        for(ItemTypes type : ItemTypes.values()) {
            if(type.getId() == id)
                return type;
        }
        return ITEM;
    }

}
