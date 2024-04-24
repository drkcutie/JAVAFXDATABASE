package com.example.javafxdatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static com.example.javafxdatabase.RegisterController.showAlert;

public class UpdateTaskController {

    public int postid;

    @FXML
    private Button btnUpdateTask;

    @FXML
    private TextArea newContent;

    @FXML
    private DatePicker newDeadline;

    @FXML
    private TextField newTitle;
    private DashboardController dashboardController;

    @FXML

    public void init (DashboardController dashboardController)
    {
        this.dashboardController = dashboardController;
    }

    void setPostid(int postid)
    {
        this.postid = postid;
    }
    @FXML
    void updateTask(ActionEvent event) {
        if (Objects.equals(newTitle.getText(), "")) {
            showAlert(Alert.AlertType.ERROR, "Missing Title", "Please provide a title.");
            return;
        }

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(
                     "UPDATE todolist SET title = ? , content  = ? , deadline = ? where id = ?"
             )) {
            String title =  newTitle.getText();
            String content =    newContent.getText();
            preparedStatement.setString(1, title); // Use the stored variable instead of getting text again
            preparedStatement.setString(2, content); // Use the stored variable instead of getting text again

            LocalDate localDate = newDeadline.getValue();
            if (localDate != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
                preparedStatement.setDate(3, sqlDate);
            } else {
                preparedStatement.setDate(3, null);
            }
            preparedStatement.setInt(4, postid);
            preparedStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "The task has been updated successfully.");
            closeUpdateTask(event);
            dashboardController.getPost();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR ,"Failed", "The task has not been updated.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }catch (DateTimeParseException e )
        {
            showAlert(Alert.AlertType.ERROR, "Failed", "Input a valid date");
        }
    }

    void closeUpdateTask(ActionEvent event)
    {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        stage.close();
    }


}
