package business;

import data.entities.Product;
import java.util.List;
import java.util.stream.Collectors;

public class PriceFilterStrategy implements FilterStrategy
{
    private double minPrice;
    private double maxPrice;

    public PriceFilterStrategy(double minPrice, double maxPrice)
    {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public List<Product> filter(List<Product> products, Object filterCriteria)
    {
        return products.stream()
                       .filter(p -> p.getPrice() >= minPrice &&
                                p.getPrice() <= maxPrice)
                       .collect(Collectors.toList());
    }
}
