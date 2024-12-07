package data;

import data.entities.Product;
import java.util.ArrayList;
import java.util.List;

public class InMemoryProductRepository implements  ProductRepository
{
    private List<Product> products = new ArrayList<>();
    private int           nextId   = 1;

//Override Functions in ProductRepository.java
    @Override
    public void addProduct(Product product)
    {
        product.setId(nextId++);
        products.add(product);
    }
    @Override
    public void updateProduct(Product product)
    {
        int index = findProductIndexById(product.getId());
        if (index != -1)
        {
            products.set(index, product);
        }
        else 
        {
            System.err.println
            (
                "InMemoryProductRepository.java, " +
                "public void updateProduct(Product product), " +
                "Product not found by id"
            );
        }
    }
    @Override
    public void removeProduct(int productId)
    {
        int index = findProductIndexById(productId);
        if (index != -1)
        {
            products.remove(index);
        }
        else
        {
           System.err.println
           (
                "InMemoryProductRepository.java, " +
                "public void removeProduct(int productId), " +
                "Product not found by id"
           );
        }
    }
    @Override
    public Product getProductById(int productId)
    {
        return products.stream()
                       .filter(p -> p.getId() == productId)
                       .findFirst()
                       .orElse(null);
    }
    @Override
    public List<Product> listAllProducts()
    {
        return (new ArrayList<>(products));
    }

//Member Function
    private int findProductIndexById(int productId)
    {
        for (int i = 0; i < products.size(); i++)
        {
            if (products.get(i).getId() == productId)
            {
                return (i);
            }
        }
        return (-1);
    }
}
