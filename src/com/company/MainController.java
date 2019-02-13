package com.company;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.text.ParsePosition;
import java.util.Properties;
import java.util.Scanner;

public class MainController {

    private static ParallelScanner in;
    private static PrintStream out;

    public TextField Phone;
    private Sms sms = new Sms();
    public CheckBox Time;
    public CheckBox Status;
    public CheckBox S8Off;
    public CheckBox Info;
    public CheckBox L1;
    public CheckBox L2;
    public CheckBox Actv;
    public CheckBox Dact;
    public TextArea textWindow;
    private Object ParsePosition;
    private Object ActionEvent;

    public MainController() throws IOException {
    }

    public void initialize() {

        final Properties properties = new Properties();


        try (InputStream is = Main.class.getResourceAsStream("config.properties")) {
            properties.load(is);

            String host = properties.getProperty("db.host");
            String port = properties.getProperty("db.port");
            Socket socket = new Socket(host, Integer.parseInt(port));


            in = new ParallelScanner(new Scanner(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            in.start();
            connection(socket, out, in);


        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private void connection(Socket socket, PrintStream out, ParallelScanner in) throws Exception {


        enterPassword(socket, out, in);
        readFromDevice();

    }

    private static void enterPassword(Socket socket, PrintStream out, ParallelScanner in) throws IOException {
//        вводим пароль, вводные данные идут из файла config.properties.login
        final Properties properties = new Properties();
        try
                (InputStream is = Main.class.getResourceAsStream("config.properties.login")) {
            properties.load(is);
            String login = properties.getProperty("cp.action");
            String username = properties.getProperty("cp.username");
            String secret = properties.getProperty("cp.secret");
            out.print("Action: " + login + "\r\nUsername: " + username + "\r\nSecret: " + secret + "\r\n\r\n");
        }



    }


    public void addPhone(ActionEvent actionEvent) {

//вводим номер и отправляем в SMS
        String phone = Phone.getText();
        sms.setPhone(phone);
        System.out.println(sms.getPhone());
        textWindow.appendText("Добавлен номер: " + sms.getPhone() + "\n\r");

    }


    @FXML
    private void readFromDevice() {

        while (in.hasNext()) {
            String line = in.nextLine();

//            System.out.println(line);

            textWindow.appendText(line +"\n");
        }


    }

    public static ParallelScanner getIn() {
        return in;
    }


    public static PrintStream getOut() {
        return out;
    }


    public void handleOptions(ActionEvent actionEvent) {

        String message = "Выбрано: \n";

        if (Time.isSelected()) {
            message += "Time\n";
//                textWindow.appendText(message);
            out.print(sms.getTime());

        }

        if (L1.isSelected()) {
            message += "L1\n";
//                textWindow.appendText(message);
            out.print(sms.getL1());

        }
        if (L2.isSelected()) {
            message += "L2\n";
//                textWindow.appendText(message);
            out.print(sms.getL2());

        }
        if (Info.isSelected()) {
            message += "Info\n";
//                textWindow.appendText(message);
            out.print(sms.getInfo());

        }
        if (Actv.isSelected()) {
            message += "Actv\n";
//                textWindow.appendText(message);
            out.print(sms.getActv());

        }
        if (Dact.isSelected()) {
            message += "Dact\n";
//                textWindow.appendText(message);
            out.print(sms.getDact());

        }
        if (S8Off.isSelected()) {
            message += "S8Off\n";
//                textWindow.appendText(message);
            out.print(sms.getS8Off());

        }
        if (Status.isSelected()) {
            message += "Status\n";

            out.print(sms.getStatus());

        }
        textWindow.appendText(message + "\n\r");


    }
}




