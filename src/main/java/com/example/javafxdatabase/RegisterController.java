package com.example.javafxdatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private Button accountRegister;


    @FXML
    private TextField registerFirstName;

    @FXML
    private TextField registerLastName;
    @FXML
    private TextField registerPassword;

    @FXML
    private TextField registerUsername;


    public static void showAlert(Alert.AlertType alertType, String title , String content)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void accountRegister(ActionEvent event) {
        String firstname = registerFirstName.getText();
        String lastname  = registerLastName.getText();
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        if(username == "" || password == "" || username.length() < 6 || password.length() < 6)
        {
            showAlert(Alert.AlertType.ERROR, "Invalid credentials", "Enter a valid username and password (Inputs should be atleast 6 characters) ");
            return;
        }
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO users (firstname , lastname , username,password) VALUES (?,?,?,?)"
            ))
        {
            statement.setString(1, firstname);
            statement.setString(2,lastname);
            statement.setString(3, username);
            statement.setString(4,password);
            int rows = statement.executeUpdate();
            System.out.println("Rows inserted: " + rows);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully");
            ResultSet resultSet = statement.getResultSet();
            backtoLogin(event);
        }catch (SQLException e)
        {
            showAlert(Alert.AlertType.ERROR,"Account exists", "Choose an another username");

        }
    }

    @FXML
    void backtoLogin(ActionEvent event) {
        try{
            Parent registerView= FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = accountRegister.getScene();
            scene.setRoot(registerView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
