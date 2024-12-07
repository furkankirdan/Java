package ui;

import business.StockManager;
import data.InMemoryProductRepository;
import data.ProductRepository;

public class Main {
    public static void main(String[] args)
    {
        ProductRepository repository = new InMemoryProductRepository();
        StockManager stockManager = new StockManager(repository);
        
        StockAppUI app = new StockAppUI(stockManager);
        app.setVisible(true);
    }
}

