package com.sky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sky.enums.Location;

@Entity
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "location", nullable = false)
    private Location location;

    public Customer()
    {
        // Spring uses this constructor
    }

    public Customer(final Location location)
    {
        this.location = location;
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public int getLocationId()
    {
        return this.location.getLocationId();
    }

    @Override
    public int hashCode()
    {
        if (this.id == null || this.location == null)
            return super.hashCode();

        return this.id.hashCode() + this.location.hashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final Customer other = (Customer) obj;
        return this.getId() == other.getId();
    }

    @Override
    public String toString()
    {
        if (this.location == null)
            return String.valueOf(this.getId());

        return Location.getLocationName(this.location.getLocationId());
    }
}