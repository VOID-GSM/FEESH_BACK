package com.feesh.post.entity;

public enum Category {
    FOOD("음식"),
    FASHION_SHOPPING("패션/쇼핑"),
    DAILY_NECESSITY("생활용품"),
    CULTURE_LEISURE("문화/여가"),
    ETC("기타");

    private final String label;

    Category(String label) {
        this.label=label;
    }
    public String getLabel() {
        return label;
    }
}