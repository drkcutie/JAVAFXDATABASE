package com.example.javafxdatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) {
        createTable();
    }

    public static void createTable()
    {
        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()){
            //batch process table
            c.setAutoCommit(false);
            String createUsersquery = "CREATE TABLE IF NOT EXISTS users(" +
                    "id int AUTO_INCREMENT PRIMARY KEY," +
                    "firstname VARCHAR(50) NOT NULL, " +
                    "lastname VARCHAR(50) NOT NULL, "+
                    "username VARCHAR(50) NOT NULL ," +
                    "password VARCHAR(100) NOT NULL, "+
                    "constraint unique_username UNIQUE(username)"+
                    ")";


            statement.execute(createUsersquery);

            String createTodolistQuery = "CREATE TABLE IF NOT EXISTS todolist(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "userid int NOT NULL ," +
                    "title VARCHAR(50) NOT NULL," +
                    "content VARCHAR(50) NOT NULL," +
                    "deadline DATE," +
                    "status ENUM('pending', 'completed') DEFAULT 'pending'," +
                    "FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE" +
                    ")";
            statement.execute(createTodolistQuery);

            c.commit();
            System.out.println("Two tables created successfully");
        }catch(SQLException e ){
            e.printStackTrace();

        }

    }
}
