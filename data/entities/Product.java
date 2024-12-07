package data.entities;

public class Product
{
    private int    id;
    private String name;
    private String category;
    private int    quantity;
    private double price;

// Constructor
    public Product
        (
            int id, 
            String name,
            String category,
            int quantity,
            double price
        )
    {
        this.id       = id;
        this.name     = name;
        this.category = category;
        this.quantity = quantity;
        this.price    = price;
    }

// getters
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getCategory()
    {
        return category;
    }
//setters
    public void setCategory(String category)
    {
        this.category = category;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
//methods
    @Override
    public String toString()
    {
        return String.format
            ("Product [ID=%d, Name=%s, Category=%s, Quantity=%d, Price=%.2f]", 
                             id, name, category, quantity, price
            );
    }
}
