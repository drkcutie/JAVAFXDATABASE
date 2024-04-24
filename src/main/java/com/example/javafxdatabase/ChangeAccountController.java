package com.example.javafxdatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.javafxdatabase.RegisterController.showAlert;

public class  ChangeAccountController{

    @FXML
    private Button accountChange;

    @FXML
    private TextField changeFirstName;

    @FXML
    private TextField changeLastName;

    @FXML
    private PasswordField changePassword;

    @FXML
    private TextField changeUsername;



    @FXML
    void accountChange(ActionEvent event) {
        String firstname = changeFirstName.getText();
        String lastname = changeLastName.getText();
        String username = changeUsername.getText();
        String password = changePassword.getText();

        if (username.isEmpty() || password.isEmpty() || username.length() < 6 || password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Invalid credentials", "Enter a valid username and password (Inputs should be at least 6 characters)");
            return;
        }

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "UPDATE users SET firstname = ?, lastname = ?, username = ?, password = ? WHERE id = ?")) {
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setInt(5, UserInformation.getInstance().getUserId());

            int rows = statement.executeUpdate();
            System.out.println("Rows updated: " + rows);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account changed successfully");
            backtoLogin(event);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Account exists", "Choose another username");
        }
    }

    void backtoLogin(ActionEvent event) {
        try{
            Parent registerView= FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = accountChange.getScene();
            scene.setRoot(registerView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
