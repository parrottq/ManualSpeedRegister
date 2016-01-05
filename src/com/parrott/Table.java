package com.parrott;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by quinn on 30/12/2015.
 */
public class Table {

    private TableView<Direction> tableView;
    private TableColumn<Direction, Integer> leftSpeedColumn;
    private TableColumn<Direction, Integer> rightSpeedColumn;
    private TableColumn<Direction, Integer> delayColumn;

    public Table(){
        leftSpeedColumn = new TableColumn<>("Left Speed");
        leftSpeedColumn.setMinWidth(200);
        leftSpeedColumn.setMaxWidth(400);
        leftSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("leftSpeed"));
        leftSpeedColumn.setSortable(false);

        rightSpeedColumn = new TableColumn<>("Right Speed");
        rightSpeedColumn.setMinWidth(200);
        rightSpeedColumn.setMaxWidth(400);
        rightSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("rightSpeed"));
        rightSpeedColumn.setSortable(false);

        delayColumn = new TableColumn<>("Delay");
        delayColumn.setMinWidth(200);
        delayColumn.setMaxWidth(400);
        delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
        delayColumn.setSortable(false);

        tableView = new TableView<>();
        tableView.getColumns().addAll(leftSpeedColumn, rightSpeedColumn, delayColumn);
    }

    public TableView getTable(){
        return tableView;
    }

}
