package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    public TextField tField;
    public ListView list;
    public TextArea textChat;
    public ClientSocketReader sc;

    public Controller() {
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        sc = new ClientSocketReader(this);
        new Thread(sc).start();
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("All");
        list.setItems(items);
    }



    public void send(ActionEvent actionEvent) throws IOException {
        Object a = list.getSelectionModel().getSelectedItem();
        String from;
        if(a != null) {
            from = a.toString();
        } else {
            from = "All";
        }
        sc.out.println("send " + from
                + " " + Client.name + " : " + tField.getText());
        tField.clear();
    }

    public void parseLine(String line) {

        if(line == null)
            return;
        String command = line.split(" ")[0];
        if(command.equals("list")) {
            ObservableList<String> items = FXCollections.observableArrayList();
            String[] names = line.split(" ");
            items.add("All");
            for(int i = 1; i < names.length; i++) {
                items.add(names[i]);
            }
            list.setItems(items);
        } else if (command.equals("send")) {
            String[] words = line.split(" ");
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 1; i < words.length; i++) {
                stringBuffer.append(words[i] + " ");
            }

            String text = textChat.getText();
            textChat.setText(text + "\n" + stringBuffer.toString());
        }
    }
}
