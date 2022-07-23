package com.rottenbeetle.testtaskforbars;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Server implements Runnable {
    private final static int BUFFER_SIZE = 256;
    private AsynchronousServerSocketChannel server;

    private final static String HEADERS =
            "HTTP/1.1 200 OK\n" +
                    "Server: naive\n" +
                    "Content-Type: text/html\n" +
                    "Content-Length: %s\n" +
                    "Connection: close\n\n";


    @Override
    public void run() {
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("127.0.0.1", 8080));

            while (true) {
                Future<AsynchronousSocketChannel> future = server.accept();
                handleClient(future);
            }
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Сервер остановлен!");
        }
    }

    private void handleClient(Future<AsynchronousSocketChannel> future)
            throws InterruptedException, ExecutionException, TimeoutException, IOException {

        AsynchronousSocketChannel clientChannel = future.get(3600, TimeUnit.SECONDS);

        while (clientChannel != null && clientChannel.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            StringBuilder builder = new StringBuilder();
            boolean keepReading = true;

            while (keepReading) {
                clientChannel.read(buffer).get();

                int position = buffer.position();
                keepReading = position == BUFFER_SIZE;

                byte[] array = keepReading
                        ? buffer.array()
                        : Arrays.copyOfRange(buffer.array(), 0, position);

                builder.append(new String(array));
                buffer.clear();
            }

            String body = getJsonFromDB();

            if (body != null) {
                String page = String.format(HEADERS, body.length()) + body;
                ByteBuffer resp = ByteBuffer.wrap(page.getBytes());
                clientChannel.write(resp);
            }

            clientChannel.close();
        }
    }

    public String getJsonFromDB() throws IOException {

        String query = "select * from contracts";
        List<Contract> contracts = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = null;
        try {
            DBWorker worker = new DBWorker();
            createAndFillingTable(worker);
            Statement statement = worker.getConnection().createStatement();

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Contract contract = new Contract();
                contract.setId(resultSet.getInt(1));
                contract.setNumber(resultSet.getInt(2));
                contract.setDate(resultSet.getDate(3));
                contract.setLastUpdate(resultSet.getDate(4));

                contracts.add(contract);
            }
            json = mapper.writeValueAsString(contracts);

        } catch (SQLException | JsonProcessingException e) {
            server.close();
            System.out.println("Не правильные параметры БД!");
        }
        return json;
    }

    private void createAndFillingTable(DBWorker worker) {
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

}