package com.sky.enums;

public enum Location
{
    LONDON(1, "London"), LIVERPOOL(2, "Liverpool");

    private int locationId;

    private String name;

    private Location(final int locationId, final String name)
    {
        this.locationId = locationId;
        this.name = name;
    }

    public int getLocationId()
    {
        return this.locationId;
    }

    public String getName()
    {
        return this.name;
    }

    // TODO migrate to Java 8
    public static String getLocationName(final int locationId) throws IllegalArgumentException
    {
        for (final Location location : Location.values())
        {
            if (locationId == location.getLocationId())
                return location.getName();
        }
        throw new IllegalArgumentException();
    }

    public static Location findById(final int locationId) throws IllegalArgumentException
    {
        for (final Location location : Location.values())
        {
            if (locationId == location.getLocationId())
                return location;
        }
        throw new IllegalArgumentException();
    }
}