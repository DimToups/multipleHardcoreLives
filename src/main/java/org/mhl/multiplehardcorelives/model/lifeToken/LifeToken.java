package org.mhl.multiplehardcorelives.model.lifeToken;

public abstract class LifeToken {
    public abstract boolean isSuperior(LifeToken tokens);

    public abstract boolean isSuperiorOrEqual(LifeToken tokens);

    public abstract boolean isInferiorOrEqual(LifeToken tokens);

    public abstract boolean isNull();

    public abstract LifeToken minus(LifeToken lifeToken);

    @Override
    public abstract String toString();
}
