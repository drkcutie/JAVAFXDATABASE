package com.example.javafxdatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {


    @FXML
    private Button btnAdd;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnSettings;

    @FXML
    private ScrollPane spContainer;

    @FXML
    private Text textWelcomeUser;

    @FXML
    private VBox vbContainer;




    @FXML
    void addTask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addTask.fxml"));
        Parent parent = loader.load();
        AddTaskController addTaskController = loader.getController();
        addTaskController.init(this);
        Stage stage = new Stage();
        stage.setTitle("Add Task");
        Scene scene = new Scene(parent);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();
    }
    public void initialize()
    {

        textWelcomeUser.setText(UserInformation.getInstance().getFirstName());
        getPost();

    }

    @FXML
    void logoutUser(ActionEvent event)
    {
        try{
            Parent registerView= FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = btnLogout.getScene();
            scene.setRoot(registerView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void goToSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent parent = loader.load();

            SettingsController settingsController = loader.getController();
            Scene currentScene = btnLogout.getScene();

            currentScene.setRoot(parent);

            settingsController.init(this, btnAdd.getScene());
            settingsController.initialize();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    void getPost()
    {
        vbContainer.getChildren().clear();

        try(Connection c = MySQLConnection.getConnection(); PreparedStatement preparedStatement =
                c.prepareStatement(
                        "SELECT * FROM todolist WHERE userid = ? " +
                 "ORDER BY id, CASE WHEN status = 'pending' THEN 1 ELSE 2 END"
                )) {

            preparedStatement.setInt(1, UserInformation.getInstance().getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("taskItem.fxml"));
                Parent root = loader.load();
                TaskItemController taskItemController = loader.getController();
                String status = resultSet.getString(6).equals("pending") ? "Pending" : "Completed";
                String date = resultSet.getDate(5) != null ? resultSet.getDate(5).toString() : "No Due Date"; // Handle null date
                taskItemController.init(this);
                taskItemController.initializeTask(resultSet.getInt(1), resultSet.getString(3), resultSet.getString(4), date, status);
                vbContainer.getChildren().add(root);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
