package com.sunjet.utils.common;

import java.util.Random;

public class RandomStringGeneratorBase {
    private int length;
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";//ABCDEFGHIJKLMNOPQRSTUVWXYZ
    private final Random rn = new Random();

    public RandomStringGeneratorBase(int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length cannot be less than or equal to 0");

        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getRandomString() {
        StringBuilder sb = new StringBuilder(this.length);

        for (int i = 0; i < this.length; i++) {
            sb.append(alphabet.charAt(rn.nextInt(alphabet.length())));
        }

        return sb.toString();
    }
}
