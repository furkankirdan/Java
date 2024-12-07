package business;

import data.ProductRepository;
import data.entities.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StockManager
{
    private Map<String, Set<Product>> searchCache = new HashMap<>();
    private ProductRepository productRepository;
    private SearchStrategy    searchStrategy;
    private FilterStrategy    filterStrategy;

//Constructor
    public StockManager(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }
//Methods
    public void addProduct(Product product)
    {
        productRepository.addProduct(product);
    }
    public void updateProduct(Product product)
    {
        productRepository.updateProduct(product);
    }
    public void removeProduct(int productId)
    {
        productRepository.removeProduct(productId);
    }
    public Product getProductById(int productId)
    {
        return productRepository.getProductById(productId);
    }
    public List<Product> listAllProducts()
    {
        return productRepository.listAllProducts();
    }
    // public List<Product> searchProducts(String keyword)
    // {
    //     return searchStrategy.search
    //            (
    //                  productRepository.listAllProducts(), keyword
    //            );
    // }
    public List<Product> searchProducts(String keyword)
    {
        if (searchCache.containsKey(keyword))
        {
        return new ArrayList<>(searchCache.get(keyword));
        }

        List<Product> allProducts = productRepository.listAllProducts();
        Set<Product> resultsSet = new HashSet<>
        (
            searchStrategy.search(allProducts, keyword)
        );

        searchCache.put(keyword, resultsSet);
        return new ArrayList<>(resultsSet);  
    }
    public List<Product> filterProducts(Object filterCriteria)
    {
        return filterStrategy.filter
                (
                    productRepository.listAllProducts(), filterCriteria
                );
    }
    public void setSearchStrategy(SearchStrategy searchStrategy)
    {
        this.searchStrategy = searchStrategy;
    }
    public void setFilterStrategy(FilterStrategy filterStrategy)
    {
        this.filterStrategy = filterStrategy;
    }
}
