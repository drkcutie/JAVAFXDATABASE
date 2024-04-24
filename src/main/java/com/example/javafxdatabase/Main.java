package com.example.javafxdatabase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        CreateTable.createTable();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),  500, 480);
        stage.setTitle("TODOLIST APP");
        stage.setMinHeight(480);
        stage.setMinWidth(600);
        stage.setMaxHeight(580);
        stage.setMaxWidth(700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}