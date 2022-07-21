package com.rottenbeetle.testtaskforbars;

import java.io.IOException;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.text.Font;

public class HelloController {

    private ObservableList<Contract> contracts;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox vboxApp;

    @FXML
    private SplitPane scrollOne;

    @FXML
    private ScrollPane scrollTwo;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Label mainLabel;

    @FXML
    private Font x5;

    @FXML
    private TableView<Contract> listContracts;

    @FXML
    private Button refreshButton;

    @FXML
    private TableColumn<Contract, String> numberContract;

    @FXML
    private TableColumn<Contract, String> date;

    @FXML
    private TableColumn<Contract, String > lastUpdate;

    @FXML
    private TableColumn<Contract, CheckBox> relevance;

    @FXML
    void initialize() throws InterruptedException {

        contracts = FXCollections.observableArrayList(getContractsFromServer());
        //Устанавливается тип и значение которое должно хранится в колонке
        numberContract.setCellValueFactory(new PropertyValueFactory<Contract, String>("number"));
        date.setCellValueFactory(new PropertyValueFactory<Contract, String>("date"));
        lastUpdate.setCellValueFactory(new PropertyValueFactory<Contract, String>("lastUpdate"));
        relevance.setCellValueFactory(new PropertyValueFactory<Contract, CheckBox>("relevance"));

        //True если дата последенего обновления договора меньше текущей на 60 дней
        for (Contract contract : contracts) {
            checkLastUpdate(contract);
        }

        //Таблица заполняется данными
        listContracts.setItems(contracts);

        refreshButton.setOnAction(event -> {
            try {
                initialize();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Contract> getContractsFromServer(){
        URL url = null;
        ObjectMapper mapper = new ObjectMapper();
        List<Contract> contracts = new ArrayList<>();
        try {
            url = new URL("http://localhost:8080/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //Проверка подключения
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();

                //JSON в список объектов
                contracts = Arrays.asList(mapper.readValue(informationString.toString(), Contract[].class));
                conn.disconnect();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return contracts;
    }

    private void checkLastUpdate(Contract contract){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(contract.getLastUpdate());
            long diff = Math.abs(System.currentTimeMillis() - date.getTime());
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            contract.getRelevance().setSelected(days <= 60);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
