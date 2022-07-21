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
        //Создание таблицы
        createAndFillingTable();
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

    private void createAndFillingTable(){
        DBWorker worker = new DBWorker();
        try {
            DatabaseMetaData md = worker.getConnection().getMetaData();
            ResultSet rs = md.getTables(null, null, "contracts", null);
            if (!rs.next()) {
                Statement statement = worker.getConnection().createStatement();
                statement.execute("CREATE TABLE contracts\n" +
                        "(\n" +
                        "    id          BIGSERIAL NOT NULL PRIMARY KEY,\n" +
                        "    number      INTEGER   NOT NULL,\n" +
                        "    date        DATE      NOT NULL,\n" +
                        "    last_update DATE      NOT NULL\n" +
                        ");" +
                        "insert into contracts (id, number, date , last_update) values (1, 10, '2000-01-08','2022-07-21');\n" +
                        "insert into contracts (id, number, date , last_update) values (2, 20, '2005-03-15','2022-08-27');\n" +
                        "insert into contracts (id, number, date , last_update) values (3, 30, '2010-06-19','2021-04-11');\n" +
                        "insert into contracts (id, number, date , last_update) values (4, 40, '2015-08-25','2022-06-13');\n" +
                        "insert into contracts (id, number, date , last_update) values (5, 50, '2013-12-25','2022-03-10');");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}