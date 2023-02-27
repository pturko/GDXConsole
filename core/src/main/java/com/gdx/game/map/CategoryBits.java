package com.gdx.game.map;

public class CategoryBits {

    public static final short GROUND = 1;

    public static final short BOX = 2;
    public static final short TORCH = 4;

    private final short value;

    CategoryBits(short value) {
        this.value = value;
    }

    public short getName() {
        return value;
    }
}