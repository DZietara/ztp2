package com.example.ztp2;

import com.example.ztp2.model.ProductModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductViewModel {
    private final ProductService productService;
    private final ObservableList<ProductModel> products;

    public ProductViewModel() {
        this.productService = new ProductService();
        this.products = FXCollections.observableArrayList();
    }

    public ObservableList<ProductModel> getProducts() {
        return products;
    }

    public void loadProducts() {
        try {
            products.setAll(productService.getAllProducts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProductModel getProductDetails(Long productId) throws Exception {
        return productService.getProductById(productId);
    }

    public void addProduct(ProductRequest productRequest) throws Exception {
        productService.addProduct(productRequest);
        loadProducts();
    }

    public void deleteProduct(ProductModel product) throws Exception {
        productService.deleteProduct(product.getId());
    }
}