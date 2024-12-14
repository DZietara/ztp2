package com.example.ztp2.common;

import com.example.ztp2.model.ProductModel;
import javafx.scene.control.Alert;

import static javafx.scene.control.Alert.*;

public class AlertService {

    public void showProductDetails(ProductModel product) {
        showAlert(
                "Product Details",
                "Details for Product: " + product.getName(),
                "ID: " + product.getId() +
                        "\nName: " + product.getName() +
                        "\nPrice: " + product.getPrice() +
                        "\nQuantity: " + product.getQuantity() +
                        "\nStatus: " + product.getStatus(),
                AlertType.INFORMATION
        );
    }

    public void showSuccess(String header, String content) {
        showAlert("Success", header, content, AlertType.INFORMATION);
    }

    public void showError(String content) {
        showAlert("Error", "An error occurred", content, AlertType.ERROR);
    }

    private void showAlert(String title, String headerText, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
