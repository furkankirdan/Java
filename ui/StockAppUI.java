package ui;

import business.PriceFilterStrategy;
import business.ProductNameSearchStrategy;
import business.StockManager;
import data.entities.Product;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StockAppUI extends JFrame
{
    private StockManager stockManager;
    private DefaultTableModel tableModel;
    private JTable productTable;

    private JTextField txtName, txtQuantity, txtPrice, txtMinPrice, txtMaxPrice;
    private JComboBox<String> categoryComboBox;
    private JButton btnUpdateProduct;

    public StockAppUI(StockManager stockManager)
    {
        this.stockManager = stockManager;
        initUI();
    }
/*
 * Kullanıcı arayüzünü oluşturur. 
 * Üst panelde ürün ekleme, güncelleme, 
 * ve arama alanları; merkezde ürün listesini gösteren tablo;
 *  alt panelde ise silme ve listeyi yenileme düğmeleri bulunur.
 */
    private void initUI()
    {
        setTitle("Stock Tracking App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(4, 4, 5, 5)); 
        txtName = new JTextField();
        txtQuantity = new JTextField();
        txtPrice = new JTextField();
        txtMinPrice = new JTextField();
        txtMaxPrice = new JTextField();

        String[] categories = 
        {
            "Electronics", "Furniture", "Grocery", "Clothing"
        };
        categoryComboBox = new JComboBox<>(categories);

        JButton btnAddProduct = new JButton("Add Product");
        btnAddProduct.addActionListener(e -> addProduct());

        btnUpdateProduct = new JButton("Update Product");
        btnUpdateProduct.setEnabled(false);
        btnUpdateProduct.addActionListener(e -> updateSelectedProduct());

        // First row - Product Name and Category
        topPanel.add(new JLabel("Product Name:"));
        topPanel.add(txtName);
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryComboBox);

        // Second row - Quantity and Price
        topPanel.add(new JLabel("Quantity:"));
        topPanel.add(txtQuantity);
        topPanel.add(new JLabel("Price:"));
        topPanel.add(txtPrice);

        // Third row - Min and Max Price
        topPanel.add(new JLabel("Min Price:"));
        topPanel.add(txtMinPrice);
        topPanel.add(new JLabel("Max Price:"));
        topPanel.add(txtMaxPrice);

        //Search button
        JButton btnSearch = new JButton("Search by Price Range");
        btnSearch.addActionListener(e -> filterProducts());
        topPanel.add(btnSearch);

        topPanel.add(btnAddProduct);
        topPanel.add(btnUpdateProduct);

        // Product Table
        String[] columnNames = {"ID", "Name", "Category", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        productTable.getSelectionModel().addListSelectionListener(e -> 
            selectProductForEdit());

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton btnDeleteProduct = new JButton("Delete Selected");
        btnDeleteProduct.addActionListener(e -> deleteSelectedProduct());

        JButton btnRefresh = new JButton("Refresh List");
        btnRefresh.addActionListener(e -> loadProductTable());

        bottomPanel.add(btnDeleteProduct);
        bottomPanel.add(btnRefresh);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        stockManager.setSearchStrategy(new ProductNameSearchStrategy());
        stockManager.setFilterStrategy(new PriceFilterStrategy(0, 1000));
        loadProductTable();
    }

/*
 * Yeni bir ürün ekler. 
 * Kullanıcıdan alınan verilerle yeni 
 * bir ürün oluşturur ve StockManager aracılığıyla sisteme ekler.
 */
    private void addProduct()
    {
        try
        {
            String name = txtName.getText();
            String category = (String) categoryComboBox.getSelectedItem();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (name.isEmpty())
            {
                showMessage("Product name cannot be empty!");
                return;
            }
            Product product = new Product(0, name, category, quantity, price);
            stockManager.addProduct(product);
            loadProductTable();
            clearInputFields();
        }
        catch (NumberFormatException e)
        {
            showMessage("Please enter valid numbers for quantity and price.");
        }
    }

/*
 * Tablo üzerinden seçilen ürünü siler. 
 * Kullanıcı tarafından seçilen satırdaki ürün StockManager'dan kaldırılır.
 */
    private void deleteSelectedProduct()
    {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1)
        {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            stockManager.removeProduct(productId);
            loadProductTable();
        }
    }

/*
* Fiyat aralığına göre ürünleri filtreler.
* Kullanıcının girdiği minimum ve maksimum fiyat
* değerlerine göre ürün listesini filtreler.
*/
    private void filterProducts()
    {
        try
        {
            double minPrice = Double.parseDouble(txtMinPrice.getText());
            double maxPrice = Double.parseDouble(txtMaxPrice.getText());
            stockManager.setFilterStrategy
            (
                new PriceFilterStrategy(minPrice, maxPrice)
            );

            List<Product> filteredProducts = stockManager.filterProducts(null);
            refreshTable(filteredProducts);
        }
        catch (NumberFormatException e)
        {
            showMessage("Please enter valid numbers for min and max price.");
        }
    }

/*
 * Ürün listesini tabloya yükler. 
 * StockManager'daki tüm ürünler alınır ve tabloya eklenir.
 */
    private void loadProductTable()
    {
        List<Product> products = stockManager.listAllProducts();
        refreshTable(products);
    }

/*
 * Ürün tablosunu günceller.
 *  Verilen ürün listesi kullanılarak tablo içeriği yenilenir.
 * 
 */
    private void refreshTable(List<Product> products)
    {
        tableModel.setRowCount(0);
        products.forEach(p -> {
            tableModel.addRow
            (
                new Object[]
                {
                    p.getId(), p.getName(), p.getCategory(),
                    p.getQuantity(), p.getPrice()
                }
            );
        });
    }


/*
 * Tablo üzerinden seçilen ürünü günceller. 
 * Kullanıcı, tablodan bir ürün seçip detaylarını 
 * değiştirebilir ve bu değişiklikler sisteme kaydedilir.
*/    
    private void updateSelectedProduct()
    {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1)
        {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            String name = txtName.getText();
            String category = (String) categoryComboBox.getSelectedItem();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtPrice.getText());

            Product updatedProduct = new Product
            (
                productId, name, category, quantity, price
            );
            stockManager.updateProduct(updatedProduct);
            loadProductTable();
            clearInputFields();
        }
    }

/**
 * Tablo üzerinden bir ürün seçildiğinde,
 * bu ürünün bilgilerini düzenlemek için üst paneldeki alanlara yükler.
*/
    private void selectProductForEdit()
    {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            txtName.setText((String) tableModel.getValueAt(selectedRow, 1));
            categoryComboBox.setSelectedItem
            (
                tableModel.getValueAt(selectedRow, 2)
            );
            txtQuantity.setText(tableModel.getValueAt
            (
                    selectedRow, 3).toString()
            );
            txtPrice.setText(tableModel.getValueAt(selectedRow, 4).toString());
            btnUpdateProduct.setEnabled(true);
        }
    }

/*
*Ürün ekleme ve güncelleme alanlarını temizler.
*/
    private void clearInputFields()
    {
        txtName.setText("");
        categoryComboBox.setSelectedIndex(0);
        txtQuantity.setText("");
        txtPrice.setText("");
        txtMinPrice.setText("");
        txtMaxPrice.setText("");
    }

/*
    Kullanıcıya bir mesaj göstermek için kullanılır 
    (örneğin, hata mesajları veya bilgilendirme mesajları).
*/

    private void showMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message);
    }
}





