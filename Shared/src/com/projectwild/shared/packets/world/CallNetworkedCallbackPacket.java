package com.projectwild.shared.packets.world;

public class CallNetworkedCallbackPacket {

    private int x, y, z;
    private String callback;
    private Object[] data;

    public CallNetworkedCallbackPacket() {}

    public CallNetworkedCallbackPacket(int x, int y, int z, String callback, Object... data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.callback = callback;
        this.data = data;
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

    public String getCallback() {
        return callback;
    }

    public Object[] getData() {
        return data;
    }

}
