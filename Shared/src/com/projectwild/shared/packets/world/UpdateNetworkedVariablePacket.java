package com.projectwild.shared.packets.world;

public class UpdateNetworkedVariablePacket {

    private int x, y, z;
    private String key;
    private Object value;

    public UpdateNetworkedVariablePacket() {}

    public UpdateNetworkedVariablePacket(int x, int y, int z, String key, Object value) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.key = key;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

}
