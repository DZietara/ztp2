package com.example.ztp2.common;

import javafx.scene.control.Alert;

public class AlertService {

    private static AlertService instance;

    private AlertService() {}

    public static AlertService getInstance() {
        if (instance == null) {
            synchronized (AlertService.class) {
                if (instance == null) {
                    instance = new AlertService();
                }
            }
        }
        return instance;
    }

    public void showSuccess(String header, String content) {
        showAlert("Success", header, content, Alert.AlertType.INFORMATION);
    }

    public void showError(String content) {
        showAlert("Error", "An error occurred", content, Alert.AlertType.ERROR);
    }

    public void showError(String header, String content) {
        showAlert("Error", header, content, Alert.AlertType.ERROR);
    }

    private void showAlert(String title, String headerText, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
