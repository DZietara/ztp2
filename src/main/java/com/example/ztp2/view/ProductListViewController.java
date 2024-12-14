package com.example.ztp2.view;

import com.example.ztp2.MainApplication;
import com.example.ztp2.common.AlertService;
import com.example.ztp2.model.ProductModel;
import com.example.ztp2.viewmodel.ProductViewModel;
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

public class ProductListViewController {

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

    public ProductListViewController() {
        AlertService alertService = AlertService.getInstance();
        this.viewModel = new ProductViewModel(alertService);
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
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    {
                        viewButton.setOnAction(event -> handleViewDetails(getTableRow().getItem()));
                        deleteButton.setOnAction(event -> handleDeleteProduct(getTableRow().getItem()));
                        editButton.setOnAction(event -> handleEditProduct(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonBox = new HBox(10);
                            buttonBox.getChildren().addAll(viewButton, editButton, deleteButton);
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
        viewModel.loadProducts();
    }

    @FXML
    public void handleAddNewProduct() {
        openAddProductForm();
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
            String price = priceField.getText();
            String quantity = quantityField.getText();

            boolean isValid = viewModel.addProduct(name, price, quantity);
            if (isValid) {
                formStage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleViewDetails(ProductModel product) {
        try {
            ProductModel productDetails = viewModel.getProductDetails(product.getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ztp2/product-details-view.fxml"));
            Parent productDetailsRoot = loader.load();

            ProductDetailsViewController controller = loader.getController();
            controller.setProductDetails(productDetails);

            MainApplication.switchScene(productDetailsRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditProduct(ProductModel product) {
        try {
            ProductModel productDetails = viewModel.getProductDetails(product.getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ztp2/product-edit-view.fxml"));
            Parent editProductRoot = loader.load();

            ProductEditViewController controller = loader.getController();
            controller.setProduct(productDetails);

            MainApplication.switchScene(editProductRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteProduct(ProductModel product) {
        try {
            viewModel.deleteProduct(product);
            productTable.getItems().remove(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}