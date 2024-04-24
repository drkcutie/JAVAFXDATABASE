package com.example.javafxdatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsController {

    @FXML
    private Button btnCredentials;

    @FXML
    private Button btnDeleteAccount;

    @FXML
    private TextArea outputAccounts;


    @FXML
    private ListView <String>listView;

    @FXML
    private Text usernameText;

    @FXML
    private Text welcomeText;
    private DashboardController dashboardController;

    private  Scene preScene;

    public void init(DashboardController dashboardController, Scene preScene
    )
    {
        this.dashboardController = dashboardController;
        this.preScene = preScene;
    }

    void initialize()
    {
        ObservableList<String> users = FXCollections.observableArrayList();

        try(Connection c = MySQLConnection.getConnection();
        PreparedStatement preparedStatement = c.prepareStatement(
                "SELECT * FROM users ORDER BY username ASC"
        )) {
            ResultSet resultSet = preparedStatement.executeQuery();


            while(resultSet.next())
            {
                String fname= resultSet.getString("firstname");
                String lname= resultSet.getString("lastname");
                String user = resultSet.getString("username");
                users.add(fname + " " + lname + " " + " (" + user +  ") ");
            }

            listView.setItems(users);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void changeCredentials(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("changeAccount.fxml"));
        Parent parent = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Change Account");
        Scene scene = new Scene(parent);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            )) {
            int id = 1;
            statement.setInt(1, UserInformation.getInstance().getUserId());

            int rows = statement.executeUpdate();
            if(rows == 0)
            {
                throw new RuntimeException("Failed");
            }

            RegisterController.showAlert(Alert.AlertType.INFORMATION, "Success", "Account Successfully Deleted");
            Parent login= FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene =btnDeleteAccount.getScene();
            scene.setRoot(login);
        } catch (SQLException | IOException e) {

            RegisterController.showAlert(Alert.AlertType.ERROR, "Failed", "Account delete failed or account not found");
            throw new RuntimeException(e);
        }
    }

    @FXML
    void backtoDashboard(ActionEvent event) throws IOException {
        Parent registerView= FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = btnDeleteAccount.getScene();
        scene.setRoot(registerView);
    }
}
