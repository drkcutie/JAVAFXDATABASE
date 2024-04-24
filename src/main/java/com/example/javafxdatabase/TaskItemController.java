package com.example.javafxdatabase;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.javafxdatabase.RegisterController.showAlert;

public class TaskItemController {

    public  int postid = 0;


//    private static List<TaskItemController> activeControllers = new ArrayList<>();
    public String title, content,deadline, status;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private Text taskDeadline;

    @FXML
    private Text taskStatus;

    @FXML
    private TextArea taskContent;

    @FXML
    private Text taskTitle;

    @FXML
    private CheckBox cbTask;
    private DashboardController dashboarController;


//    public TaskItemController()
//    {
//        activeControllers.add(this);
//    }
    //constructor wont work becuase fxml automatically creates a controller

    public void init(DashboardController dashboardController)
    {
        this.dashboarController = dashboardController;
    }
    void initializeTask(int postid , String title, String content, String deadline, String status)
    {
        this.postid = postid;
        this.title = title;
        this.content = content;
        this.deadline= deadline;
        this.status = status;

        taskTitle.setText(title);
        taskContent.setText(content);
        taskDeadline.setText(deadline);
        taskStatus.setText(status);
    }
    @FXML
    void deleteTask(ActionEvent event) throws SQLException {

        Connection c = null;
        try  {
            c = MySQLConnection.getConnection();
            c.setAutoCommit(false);

            PreparedStatement selectStatement = c.prepareStatement("SELECT title, content FROM todolist WHERE id = ?");
            selectStatement.setInt(1, postid);
            ResultSet res = selectStatement.executeQuery();

            PreparedStatement deleteStatement = c.prepareStatement("DELETE FROM todolist WHERE id = ?");
            deleteStatement.setInt(1, postid);
            int rowsAffected = deleteStatement.executeUpdate();

            c.commit();

            String titletemp = "";
            if (rowsAffected > 0) {
                if (res.next()) {
                    titletemp = res.getString("title");
                    System.out.println("Title:  " + titletemp);
                    System.out.println("Content: " + res.getString("content"));
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Task "+ titletemp +  " successfully deleted");
            } else {
                System.out.println("No rows were deleted.");
                c.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting: " + e.getMessage());
            c.rollback();
        }
        dashboarController.getPost();
    }


    @FXML
    void updateTask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("updateTask.fxml"));
        Parent parent = loader.load();
        UpdateTaskController updateTaskController = loader.getController();
        updateTaskController.setPostid(postid);
        updateTaskController.init(dashboarController);
        Stage stage = new Stage();
        stage.setTitle("Update Task");
        Scene scene = new Scene(parent);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void checkTask(ActionEvent event)
    {
//        System.out.println("TEST");
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE todolist SET status = ? WHERE id = ?"
            )) {
            int id = 1;
            String status = cbTask.isSelected() ? "completed" : "pending";
            taskStatus.setText(cbTask.isSelected()? "Completed" : "Pending");
            statement.setString(1, status);
            statement.setInt(2, postid);
            int rows = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to update task");
            throw new RuntimeException(e);
        }
    }


    //    public static void stopAllTasks() {
//        //TODO INTERRUPT TASK
//        for (TaskItemController controller : activeControllers) {
//            //
//        }
//        // Clear the list of active controllers
//        activeControllers.clear();
//    }

}
