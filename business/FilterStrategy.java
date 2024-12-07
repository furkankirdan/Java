package business;

import data.entities.Product;
import java.util.List;

public interface FilterStrategy
{
    List<Product> filter(List<Product> products, Object filterCriteria);
}