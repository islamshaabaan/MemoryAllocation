package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;


public class Controller implements Initializable {

    public TextField processInput;

    public TableView<Memory> table;


    public ComboBox comboBox;
    public VBox chartVBox;
    public Label comboErrorLabel;
    public Label nameLabel;
    public Label burstLabel;
    public Label priorityLabel;
    public TextField quantumInput;
    public Label quantumError;
    public HBox buttonsBox;
    public BorderPane root;
    public VBox legendVBox;
    public Label numberError;
    public TextField numberInput;
    public MenuItem play;
    public TextField addInput;
    public TextField sizeInput;
    public TextField SegNameInput;
    public TextField processInput1;
    public TextField segSizeInput;


    public TableColumn startColumn;
    public TableColumn sizeColumn;
    public TableColumn segNameColumn;
    public TableColumn segSizeColumn;
    public TableColumn processColumn;


    GanttChart<Number,String> chart;


    public void deleteButtonClicked() {

    }

    //called as soon as the layout loads
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getStylesheets().add(getClass().getResource("Viper.css").toExternalForm());

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        comboBox.getItems().add("Best Fit");
        comboBox.getItems().add("First Fit");
        comboBox.getItems().add("Worst Fit");

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        chart = new GanttChart<>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);

    /*    chart.setTitle("Gantt Chart");
        chart.setTitleSide(Side.LEFT);*/
        chart.setLegendVisible(false);
        chart.setBlockHeight(400);
        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());
        chartVBox.getChildren().add(chart);
        chart.rotateProperty().setValue(90);
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(0,"", new GanttChart.ExtraData( 1000,"status-darkRed")));
        chart.getData().addAll(series1);
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartingAdd"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        segNameColumn.setCellValueFactory(new PropertyValueFactory<>("SegmentName"));
        segSizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        //processColumn.setCellValueFactory(new PropertyValueFactory<>("SegmentName"));



    }



    public void resetButtonClicked() {

    }


    public void addHoleButtonClicked() {

        Memory AllocMemory=new Memory(500);
        String address = addInput.getText();
        String size = sizeInput.getText();
        List<Node> Holes= new ArrayList<Node>();
        Node node1 = new Node(parseInt(address), parseInt(size),null,N.Hole);
        Holes.add(node1);
        AllocMemory.PutHoles(Holes.get(0).StartingAdd, Holes.get(0).size);

        table.getItems().add(AllocMemory);
       // table.getItems().add(node1);


    }

    public void addSegmentButtonClicked(ActionEvent actionEvent) {
        String size = sizeInput.getText();
        String SegmentName = SegNameInput.getText();
        List<List<Node>> NewProcesses = new ArrayList<List<Node>>();
        NewProcesses.add(new ArrayList<Node>());

        Node node2 = new Node(0, parseInt(size),null,N.Segment,SegmentName);
        NewProcesses.get(0).add(node2);
      //  table.getItems().add(NewProcesses.get(0).get(0));



    }
}
