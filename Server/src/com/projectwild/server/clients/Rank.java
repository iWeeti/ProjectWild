package com.projectwild.server.clients;

public enum Rank {

    USER(0, "user"),
    MOD(1, "mod"),
    DEVELOPER(100, "dev");

    private int power;
    private String identifier;

    Rank(int power, String rank) {
        this.power = power;
        this.identifier = rank;
    }

    public int getPower() {
        return power;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static Rank getRank(String identifier) {
        for(Rank r : Rank.values()) {
            if(r.getIdentifier().toLowerCase().equals(identifier.toLowerCase()))
                return r;
        }
        return USER;
    }

}
