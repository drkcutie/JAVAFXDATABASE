package com.example.javafxdatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    private Stage primaryStage;
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private Text field;

    @FXML
    private TextField fieldPassword;

    @FXML
    private TextField fieldUsername;

    public static String username;
    public static String password;
    @FXML
    void loginAccount(ActionEvent event) {
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "SELECT * FROM users WHERE username =? and password = ?"
            )) {
            statement.setString(1, fieldUsername.getText());
            statement.setString(2, fieldPassword.getText());

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                System.out.println("Successfully login");
                username = fieldUsername.getText();
                password = fieldPassword.getText();
                Parent dashboard= FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Scene scene = btnLogin.getScene();
                scene.setRoot(dashboard);
            }
            else
            {
                RegisterController.showAlert(Alert.AlertType.ERROR,"Incorrect Credentials", "Wrong Username or Password");
                System.out.println("Incorrect credentials");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void registerAccount(ActionEvent event) {
        try{
            Parent registerView= FXMLLoader.load(getClass().getResource("register.fxml"));
            Scene scene = btnRegister.getScene();
            scene.setRoot(registerView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
