package com.example.ztp2;

import com.example.ztp2.model.ProductModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class ProductDetailsViewController {

    @FXML
    private Label productDetailsLabel;

    public void setProductDetails(ProductModel product) {
        productDetailsLabel.setText(
                "ID: " + product.getId() +
                        "\nName: " + product.getName() +
                        "\nPrice: " + product.getPrice() +
                        "\nQuantity: " + product.getQuantity() +
                        "\nStatus: " + product.getStatus()
        );
    }

    @FXML
    private void handleBack() throws IOException {
        MainApplication.loadProductListView();
    }
}
