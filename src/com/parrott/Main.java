package com.parrott;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private Stage window;
    private Scene scene;

    private BorderPane layout;
    private MenuBar menuBar;

    private Table table;

    public static void main(String[] args) {
	 launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Manual Speed Register");

        table = new Table();
        setupMenu();

        layout = new BorderPane();
        layout.setCenter(table.getTable());
        layout.setTop(menuBar);

        scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }

    //TODO: Make ObservableList wrapper class
    private void setupMenu(){
        MenuItem newFileItem = new MenuItem("New");
        newFileItem.setOnAction(e -> table.getTable().setItems(FXCollections.emptyObservableList()));
        MenuItem openFileItem = new MenuItem("Open...");
        openFileItem.setOnAction(e -> openActionHandler());
        MenuItem saveFileItem = new MenuItem("Save...");
        saveFileItem.setOnAction(e -> Saver.save(table.getTable().getItems(), window));
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().setAll(newFileItem, openFileItem, saveFileItem);


        MenuItem upEditMenu = new MenuItem("Move Up");
        upEditMenu.setOnAction(e ->moveUpActionHandler());
        MenuItem downEditMenu = new MenuItem("Move Down");
        downEditMenu.setOnAction(e ->moveDownActionHandler());
        MenuItem addEditMenu = new MenuItem("Add...");
        addEditMenu.setOnAction(e -> addMenuActionHandler());
        MenuItem delEditMenu = new MenuItem("Delete");
        delEditMenu.setOnAction(e -> delMenuActionHendler());

        //TODO: Remove populate MenuItem
        MenuItem populate = new MenuItem("Populate");
        populate.setOnAction(e -> {table.getTable().getItems().addAll(new Direction(1,1,1), new Direction(1,1,2), new Direction(1,1,3), new Direction(1,1,4), new Direction(1,1,5));});

        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(upEditMenu, downEditMenu, addEditMenu, delEditMenu, populate);


        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu);
    }

    private void  openActionHandler(){
        table.getTable().setItems(Saver.open(window));
    }

    private void moveUpActionHandler(){
        reverseObservable();

        TableView tableView = table.getTable();
        Direction selected = (Direction) tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getItems().indexOf(selected);

        tableView.getSelectionModel().getSelectedItems().forEach(tableView.getItems()::remove);

        ObservableList<Direction> list = FXCollections.observableArrayList();
        boolean isPresent = false;
        for (Object object : tableView.getItems()){
            Direction direction = (Direction) object;
            list.add(direction);
            if(tableView.getItems().indexOf(direction) == index) {
                list.add(selected);
                isPresent = true;
            }
        }
        if(!isPresent) {
            list.add(selected);
        }

        tableView.setItems(list);
        tableView.getSelectionModel().select(selected);

        reverseObservable();

    }

    private void moveDownActionHandler(){
        TableView tableView = table.getTable();
        Direction selected = (Direction) tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getItems().indexOf(selected);

        tableView.getSelectionModel().getSelectedItems().forEach(tableView.getItems()::remove);

        ObservableList<Direction> list = FXCollections.observableArrayList();
        boolean isPresent = false;
        for (Object object : tableView.getItems()){
            Direction direction = (Direction) object;
            list.add(direction);
            if(tableView.getItems().indexOf(direction) == index) {
                list.add(selected);
                isPresent = true;
            }
        }
        if(!isPresent) {
            list.add(selected);
        }

        tableView.setItems(list);
        tableView.getSelectionModel().select(selected);

    }

    private void delMenuActionHendler(){
        TableView tableView = table.getTable();
        tableView.getSelectionModel().getSelectedItems().forEach(tableView.getItems()::remove);
    }

    boolean wasClosed = false;
    private void addMenuActionHandler(){
        Stage dialog = new Stage(StageStyle.UTILITY);
        dialog.setTitle("Add direction");

        TextField leftSpeed = new TextField();
        leftSpeed.setPromptText("Left speed");
        TextField rightSpeed = new TextField();
        rightSpeed.setPromptText("Right speed");
        TextField delay = new TextField();
        delay.setPromptText("Delay");

        Button go = new Button("Add");
        go.setMinWidth(50);
        go.setOnAction(closer -> consumer(dialog, closer, false));

        HBox box = new HBox(leftSpeed, rightSpeed, delay, go);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(5);

        Scene dialogScene = new Scene(box);
        dialog.setScene(dialogScene);
        dialog.sizeToScene();
        dialog.setOnCloseRequest(closer -> consumer(dialog, closer, true));
        dialog.showAndWait();

        if (checkBoxes(rightSpeed.getText(), leftSpeed.getText(), delay.getText()) && !wasClosed){
            table.getTable().getItems().add(new Direction(Integer.parseInt(rightSpeed.getText()), Integer.parseInt(leftSpeed.getText()), Integer.parseInt(delay.getText())));
        }

        dialog.close();
    }

    private void consumer(Stage dialog, Event event, boolean was){
        wasClosed = was;
        dialog.hide();
        event.consume();
    }

    private boolean checkBoxes(String right, String left, String delay){
        int rightI;
        int leftI;
        if (isInteger(right) && isInteger(left) && isInteger(delay)){
            rightI = Integer.parseInt(right);
            leftI = Integer.parseInt(left);
            if (rightI >= 0 && rightI <= 255 && leftI >= 0 && leftI <= 255){
                return true;
            }
        }
        return false;
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private void reverseObservable(){
        ObservableList<Direction> list = table.getTable().getItems();
        ObservableList<Direction> newList = FXCollections.observableArrayList();
        for (Object object : list) {
            newList.add(0, (Direction) object);
        }
        table.getTable().setItems(newList);
    }

}
