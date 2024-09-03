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

    @Override
    public boolean isSuperior(LifeToken tokens) {
        if(tokens.getClass() == NumericLifeToken.class)
            return remainingLives == ((NumericLifeToken) tokens).getRemainingLives();
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isSuperiorOrEqual(LifeToken tokens) {
        if(tokens.getClass() == NumericLifeToken.class)
            return remainingLives >= ((NumericLifeToken) tokens).getRemainingLives();
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isInferiorOrEqual(LifeToken tokens) {
        if(tokens.getClass() == NumericLifeToken.class)
            return remainingLives <= ((NumericLifeToken) tokens).getRemainingLives();
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isNull() {
        return this.remainingLives <= 0;
    }

    @Override
    public LifeToken minus(LifeToken tokens) {
        if(tokens.getClass() == NumericLifeToken.class)
            return new NumericLifeToken(this.remainingLives - ((NumericLifeToken) tokens).remainingLives);
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return String.valueOf(this.remainingLives);
    }
}
