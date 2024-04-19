module com.example.javafxdatabase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.javafxdatabase to javafx.fxml;
    exports com.example.javafxdatabase;
}