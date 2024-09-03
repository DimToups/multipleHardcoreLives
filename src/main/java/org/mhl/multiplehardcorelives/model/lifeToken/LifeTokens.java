package org.mhl.multiplehardcorelives.model.lifeToken;

public enum LifeTokens {
    NumericLifeToken("Numeric"),
    ;
    private final String name;
    LifeTokens(String name){
        this.name = name;
    }

    public String getCleanName() {
        return name;
    }
}
