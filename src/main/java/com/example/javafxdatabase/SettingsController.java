package com.example.javafxdatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {

    @FXML
    private Button btnCredentials;

    @FXML
    private Button btnDeleteAccount;

    @FXML
    private TextArea outputAccounts;

    @FXML
    private Text usernameText;

    @FXML
    private Text welcomeText;


    void initialize(String username)
    {

    }

    @FXML
    void changeCredentials(ActionEvent event) {

    }

    @FXML
    void deleteAccount(ActionEvent event) {
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM users WHERE username = ? RETURNING *"
            )) {
            int id = 1;
            statement.setString(1, UserInformation.getInstance().getUsername());
            int rows = statement.executeUpdate();
            ResultSet res= statement.getResultSet();
            if(res.next())
            {
                System.out.println("Name: " +res.getString("username"));
                System.out.println("Email" +res.getString("password"));
            }
            RegisterController.showAlert(Alert.AlertType.INFORMATION, "Success", "Account Successfully Deleted");
            Parent login= FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene =btnDeleteAccount.getScene();
            scene.setRoot(login);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
