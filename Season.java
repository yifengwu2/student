package com.stupra;

public enum Season {
    SPRING("春暖花开"), SUMMER("感觉适合"), AUTUMN("秋风扫落叶"), WINTER("太冷了");
    private String context;

    Season(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }
}
