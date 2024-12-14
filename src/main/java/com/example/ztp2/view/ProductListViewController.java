package com.example.ztp2.view;

import com.example.ztp2.common.AlertService;
import com.example.ztp2.MainApplication;
import com.example.ztp2.viewmodel.ProductViewModel;
import com.example.ztp2.model.ProductModel;
import com.example.ztp2.model.ProductRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.math.BigDecimal;

public class ProductViewController {

    @FXML
    private TableView<ProductModel> productTable;

    @FXML
    private TableColumn<ProductModel, Long> idColumn;

    @FXML
    private TableColumn<ProductModel, String> nameColumn;

    @FXML
    private TableColumn<ProductModel, String> statusColumn;

    @FXML
    private TableColumn<ProductModel, Integer> quantityColumn;

    @FXML
    private TableColumn<ProductModel, BigDecimal> priceColumn;

    private final ProductViewModel viewModel;
    private final AlertService alertService;

    public ProductViewController() {
        this.viewModel = new ProductViewModel();
        this.alertService = new AlertService();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<ProductModel, Void> actionColumn = new TableColumn<>("Actions");

        actionColumn.setCellFactory(new Callback<TableColumn<ProductModel, Void>, TableCell<ProductModel, Void>>() {
            @Override
            public TableCell<ProductModel, Void> call(TableColumn<ProductModel, Void> param) {
                return new TableCell<ProductModel, Void>() {
                    private final Button viewButton  = new Button("View Details");
                    private final Button deleteButton = new Button("Delete");

                    {
                        viewButton.setOnAction(event -> handleViewDetails(getTableRow().getItem()));
                        deleteButton.setOnAction(event -> handleDeleteProduct(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonBox = new HBox(10);
                            buttonBox.getChildren().addAll(viewButton, deleteButton);
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        });

        productTable.getColumns().add(actionColumn);
        productTable.setItems(viewModel.getProducts());
        viewModel.loadProducts();

    }

    @FXML
    public void handleRefresh() {
        try {
            viewModel.loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddNewProduct() {
        try {
            openAddProductForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAddProductForm() {
        VBox form = new VBox(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button submitButton = new Button("Add Product");
        submitButton.setOnAction(event -> handleAddProduct(nameField, priceField, quantityField));

        form.getChildren().addAll(nameField, priceField, quantityField, submitButton);

        Stage formStage = new Stage();
        formStage.setTitle("Add New Product");
        formStage.setScene(new Scene(form, 300, 200));
        formStage.show();
    }

    private void handleAddProduct(TextField nameField, TextField priceField, TextField quantityField) {
        Stage formStage = (Stage) nameField.getScene().getWindow();

        try {
            String name = nameField.getText();
            BigDecimal price = new BigDecimal(priceField.getText());
            Integer quantity = Integer.parseInt(quantityField.getText());

            ProductRequest productRequest = new ProductRequest(name, price, quantity);

            viewModel.addProduct(productRequest);
            alertService.showSuccess("Product added successfully", "New product has been added.");

            productTable.setItems(viewModel.getProducts());
            formStage.close();
        } catch (Exception e) {
            alertService.showError("Failed to add product");
            e.printStackTrace();
        }
    }

    private void handleViewDetails(ProductModel product) {
        try {
            // Pobranie szczegółów produktu
            ProductModel productDetails = viewModel.getProductDetails(product.getId());

            // Załadowanie widoku szczegółów produktu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ztp2/product-details-view.fxml"));
            Parent productDetailsRoot = loader.load();

            // Pobranie kontrolera i przekazanie danych produktu
            ProductDetailsViewController controller = loader.getController();
            controller.setProductDetails(productDetails);

            // Przełączenie sceny na widok szczegółów produktu
            MainApplication.switchScene(productDetailsRoot);
        } catch (Exception e) {
            alertService.showError("Failed to load product details view");
            e.printStackTrace();
        }
    }

    private void handleDeleteProduct(ProductModel product) {
        try {
            if (product != null) {
                viewModel.deleteProduct(product);
                productTable.getItems().remove(product);
                alertService.showSuccess(
                        "Product deleted successfully",
                        String.format("%d. %s has been deleted", product.getId(), product.getName())
                );
            }
        } catch (Exception e) {
            alertService.showError("Failed to delete product ID: " + product.getId());
            e.printStackTrace();
        }
    }
}