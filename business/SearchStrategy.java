package business;

import data.entities.Product;
import java.util.List;

public interface SearchStrategy
{
    List<Product> search(List<Product> products, String keyword);
}