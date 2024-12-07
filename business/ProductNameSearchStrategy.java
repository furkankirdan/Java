package business;

import data.entities.Product;
import java.util.List;
import java.util.stream.Collectors;

public class ProductNameSearchStrategy implements SearchStrategy
{
    @Override
    public List<Product> search(List<Product> products, String keyword)
    {
        return products.stream()
                       .filter(p -> p.getName().toLowerCase().contains
                                                        (keyword.toLowerCase()))
                       .collect(Collectors.toList());
    }
}
