package data;

import data.entities.Product;
import java.util.List;

public interface ProductRepository
{
    void          addProduct(Product product);
    void          updateProduct(Product product);
    void          removeProduct(int productId);
    Product       getProductById(int productId);
    List<Product> listAllProducts();
}
