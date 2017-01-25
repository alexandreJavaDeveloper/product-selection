package com.sky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sky.enums.Category;
import com.sky.enums.Location;

@Entity
public class Product implements Comparable<Product>
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "location", nullable = true)
    private Location location;

    public Product()
    {
        // Springs uses this constructor
    }

    public Product(final String name, final Category category, final Location location)
    {
        this.name = name;
        this.category = category;
        this.location = location;
    }

    public Product(final String name, final Category category)
    {
        this.name = name;
        this.category = category;
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public Location getLocation()
    {
        return this.location;
    }

    @Override
    public int compareTo(final Product anotherProduct)
    {
        if (anotherProduct == null)
            return 1;
        return this.id.intValue() - anotherProduct.id.intValue();
    }

    @Override
    public int hashCode()
    {
        if (this.id == null || this.category == null)
            return super.hashCode();

        return this.id.hashCode() + this.category.hashCode();
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

        final Product other = (Product) obj;
        return this.getId() == other.getId();
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}