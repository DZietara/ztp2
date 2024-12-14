package com.example.ztp2.viewmodel;

import com.example.ztp2.common.AlertService;
import com.example.ztp2.common.ParseUtils;
import com.example.ztp2.model.ProductModel;
import com.example.ztp2.model.ProductRequest;
import com.example.ztp2.model.ProductService;
import com.example.ztp2.viewmodel.validation.ProductValidator;
import com.example.ztp2.viewmodel.validation.ValidationResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductViewModel {
    private final ProductService productService;
    private final ObservableList<ProductModel> products;
    private final AlertService alertService;

    public ProductViewModel(AlertService alertService) {
        this.productService = new ProductService();
        this.products = FXCollections.observableArrayList();
        this.alertService = alertService;
    }

    public ObservableList<ProductModel> getProducts() {
        return products;
    }

    public void loadProducts() {
        try {
            products.setAll(productService.getAllProducts());
        } catch (Exception e) {
            alertService.showError("Failed to load products");
            e.printStackTrace();
        }
    }

    public ProductModel getProductDetails(Long productId) {
        try {
            return productService.getProductById(productId);
        } catch (Exception e) {
            alertService.showError("Failed to load product");
            e.printStackTrace();
        }
        return null;
    }

    public boolean addProduct(String name, String priceText, String quantityText) {
        try {
            ProductRequest productRequest = new ProductRequest(
                    name,
                    ParseUtils.parseBigDecimal(priceText),
                    ParseUtils.parseInt(quantityText)
            );

            ValidationResult validationResult = ProductValidator.validate(productRequest);
            if (!validationResult.isValid()) {
                alertService.showError("Validation Error", validationResult.getErrorMessage());
                return false;
            }

            productService.addProduct(productRequest);
            loadProducts();
            alertService.showSuccess("Product added successfully", "New product has been added.");
        } catch (Exception e) {
            alertService.showError("Failed to add product");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateProduct(Long productId, String name, String priceText, String quantityText) {
        try {
            ProductRequest productRequest = new ProductRequest(
                    name,
                    ParseUtils.parseBigDecimal(priceText),
                    ParseUtils.parseInt(quantityText)
            );

            ValidationResult validationResult = ProductValidator.validate(productRequest);
            if (!validationResult.isValid()) {
                alertService.showError("Validation Error", validationResult.getErrorMessage());
                return false;
            }

            productService.updateProduct(productId, productRequest);
            loadProducts();
            alertService.showSuccess("Product updated successfully", "The product was successfully updated.");
        } catch (Exception e) {
            alertService.showError("Failed to update product");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void deleteProduct(ProductModel product) {
        try {
            productService.deleteProduct(product.getId());
            loadProducts();
            alertService.showSuccess("Product deleted successfully", "The product was deleted.");
        } catch (Exception e) {
            alertService.showError("Failed to delete product");
            e.printStackTrace();
        }
    }
}