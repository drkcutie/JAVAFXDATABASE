package com.example.javafxdatabase;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

import static com.example.javafxdatabase.RegisterController.showAlert;

public class AddTaskController {

    @FXML
    private Button btnAddTask;

    @FXML
    private TextArea contentTextArea;

    @FXML
    private DatePicker deadlineDatePicker;

    @FXML
    private TextField taskNameTextField;
    private DashboardController dashboardController;

    @FXML
    public void init(DashboardController dashboardController)
    {
        this.dashboardController = dashboardController;
    }


    @FXML
    void addTask(ActionEvent event) {
//        System.out.println("TESTING IF SUCCESS");
        if (Objects.equals(taskNameTextField.getText(), "")) {
            showAlert(Alert.AlertType.ERROR, "Missing Title", "Please provide a title.");
            return;
        }
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "INSERT INTO todolist (userid, title, content, deadline) VALUES  (?, ? ,? , ?)"
             )) {
            String title = taskNameTextField.getText();
            String content = contentTextArea.getText();
            preparedStatement.setInt(1, UserInformation.getInstance().getUserId());
            preparedStatement.setString(2, title); // Use the stored variable instead of getting text again
            preparedStatement.setString(3, content); // Use the stored variable instead of getting text again

            LocalDate localDate = deadlineDatePicker.getValue();
            if (localDate != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
                preparedStatement.setDate(4, sqlDate);
            } else {
                preparedStatement.setDate(4, null);
            }

            preparedStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "The task has been added successfully.");
            closeAddTask(event);
            dashboardController.getPost();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }catch (DateTimeParseException e)
        {
            showAlert(Alert.AlertType.ERROR, "Failed" , "Invalid Date");
        }
    }


    void closeAddTask(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        stage.close();
    }
    public static void main(String[] args) {

    }

}
