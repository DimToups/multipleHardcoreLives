package org.mhl.multiplehardcorelives.model.lifeToken;

public class NumericLifeToken extends LifeToken {
    private int remainingLives;

    public NumericLifeToken(){
         remainingLives = 5;
    }

    public NumericLifeToken(int i) {
        remainingLives = i;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

    public void setRemainingLives(int remainingLives) {
        this.remainingLives = remainingLives;
    }
}
