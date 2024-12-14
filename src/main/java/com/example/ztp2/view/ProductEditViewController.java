package com.example.ztp2.view;

import com.example.ztp2.MainApplication;
import com.example.ztp2.common.AlertService;
import com.example.ztp2.model.ProductModel;
import com.example.ztp2.viewmodel.ProductViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ProductEditViewController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button editButton;

    @FXML
    private Button backButton;

    private ProductModel product;
    private final ProductViewModel viewModel;

    public ProductEditViewController() {
        AlertService alertService = AlertService.getInstance();
        this.viewModel = new ProductViewModel(alertService);
    }

    @FXML
    public void initialize() {
        if (product != null) {
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
        }

        editButton.setOnAction(event -> handleEditProduct());
        backButton.setOnAction(event -> {
            try {
                handleBack();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void setProduct(ProductModel product) {
        this.product = product;
        initialize();
    }

    private void handleEditProduct() {
        try {
            Long id = product.getId();
            String name = nameField.getText();
            String price = priceField.getText();
            String quantity = quantityField.getText();

            boolean isValid = viewModel.updateProduct(id, name, price, quantity);
            if (isValid) {
                MainApplication.loadProductListView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        MainApplication.loadProductListView();
    }
}