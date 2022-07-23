package com.rottenbeetle.testtaskforbars;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Запуск сервера
        Thread thread = new Thread(new Server());
        thread.start();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 568, 564);
        ImageView iv = new ImageView(getClass().getResource("/icon.jpg").toExternalForm());
        stage.getIcons().add(iv.getImage());
        stage.setTitle("Список договоров");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                Platform.exit();
                thread.interrupt();
                System.out.println("Приложение закрыто!");
            }
        });
    }



    public static void main(String[] args) {
        launch();
    }
}