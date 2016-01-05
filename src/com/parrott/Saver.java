package com.parrott;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by quinn on 02/01/2016.
 */
public class Saver {

    public static void save(ObservableList<Direction> directions, Stage window){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(window);

        String data = "";
        for (Object object : directions){
            Direction direction = (Direction) object;

            data = data + direction.getLeftSpeed() + ':' + direction.getRightSpeed() + ':' + direction.getDelay() + ':';
        }

        saveFile(data, file);
    }

    public static ObservableList<Direction> open(Stage window){
        String data = openFile(new FileChooser().showOpenDialog(window));

        ObservableList<Direction> directions = FXCollections.observableArrayList();
        int right = 0;
        int left = 0;
        int delay = 0;
        char mode = 'r';
        String buffer = "";
        for (char character : data.toCharArray()) {
            if (character == ':') {
                if (mode == 'r'){
                    right = Integer.parseInt(buffer);
                    buffer = "";
                    mode = 'l';
                } else if(mode == 'l'){
                    left = Integer.parseInt(buffer);
                    buffer = "";
                    mode = 'd';
                } else if(mode == 'd'){
                    delay = Integer.parseInt(buffer);
                    buffer = "";
                    mode = 'r';
                    directions.add(new Direction(right, left, delay));
                }
            }else{
                buffer += character;
            }
        }
        return directions;
    }

    private static void saveFile(String data, File file){
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String openFile(File file){
        try {
            String data = new String(Files.readAllBytes(file.toPath()));
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
