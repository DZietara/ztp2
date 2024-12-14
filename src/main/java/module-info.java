module com.example.ztp2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens com.example.ztp2 to javafx.fxml;
    exports com.example.ztp2;
    exports com.example.ztp2.model;
    opens com.example.ztp2.model to javafx.fxml;
    exports com.example.ztp2.view;
    opens com.example.ztp2.view to javafx.fxml;
    exports com.example.ztp2.viewmodel;
    opens com.example.ztp2.viewmodel to javafx.fxml;
    exports com.example.ztp2.common;
    opens com.example.ztp2.common to javafx.fxml;
    exports com.example.ztp2.viewmodel.validation;
    opens com.example.ztp2.viewmodel.validation to javafx.fxml;
}