package com.sky.enums;

public enum Category
{
    SPORTS(1, "Sports"), NEWS(2, "News");

    private int id;

    private String name;

    private Category(final int id, final String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }
}