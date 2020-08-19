package com.projectwild.server.clients;

public enum Rank {

    USER(0, "user", null),
    MOD(1, "mod", "[YELLOW][M]"),
    DEVELOPER(100, "dev", "[RED][D]");

    private int power;
    private String identifier;
    private String tag;

    Rank(int power, String rank, String tag) {
        this.power = power;
        this.identifier = rank;
        this.tag = tag;
    }

    public int getPower() {
        return power;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getTag() {
        return tag;
    }

    public static Rank getRank(String identifier) {
        for(Rank r : Rank.values()) {
            if(r.getIdentifier().toLowerCase().equals(identifier.toLowerCase()))
                return r;
        }
        return USER;
    }

}
