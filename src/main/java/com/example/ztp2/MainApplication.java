package com.example.ztp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        loadProductListView();
    }

    public static void loadProductListView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("product-list-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        primaryStage.setTitle("Product List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void switchScene(Parent root) {
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}