module com.example.ztp2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ztp2 to javafx.fxml;
    exports com.example.ztp2;
}