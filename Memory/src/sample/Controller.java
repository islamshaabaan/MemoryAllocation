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
import javafx.util.Callback;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public TextField processInput;

    public TableView<Node> table;
    public TableView<Node> table1;

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
    public TableColumn startColumn;
    public TableColumn sizeColumn;
    public TableColumn nameColumn;
    public TableColumn processColumn;
    public TableColumn segmentColumn;
    public TableColumn sizeColumn1;
    public TextField processInput1;
    public TextField segmentName;
    public TextField segmentSize;
    public TextField memorySizeInput;
    public TextField ProcessName;
    public TextField sizeProcess;
    public TextField adressProcess;
    public TextField segegmentName;
    public TableView<Process> WaitingTable;
    public TableColumn waitingProcess;
    GanttChart<Number,String> chart;
    public ObservableList<Node> segments ;
    public ObservableList<Process> Waitings ;
    public Memory Memory1=new Memory();
    public List<Process> NewProcess=new ArrayList<>();
    public List<Process> TotalProcess=new ArrayList<>();

    public static String[] colors = {"status-darkRed","status-green","status-blue","status-yellow","status-black",
            "status-brown","status-foshia","status-bate5y","status-smawy","status-nescafe","status-orange",
            "status-red","status-lamony","status-holoOrange","status-purple","status-move","status-white"};
    public int inIndexOf(String name,ArrayList<Process> processes){
        int index=-1;
        for(int i=0;i<processes.size();i++){
            if(name.compareTo(processes.get(i).getProcessName())==0){
                return i;
            }
        }
        return index;
    }
    public int in_Index_Of(Node s,ObservableList<Node> segments){
        int index=-1;
        for(int i=0;i<segments.size();i++){
            if(s.getNodeName().compareTo(segments.get(i).getNodeName())==0&&s.getProcessName().compareTo(segments.get(i).getProcessName())==0){
                return i;
            }
        }
        return index;
    }



    //called as soon as the layout loads
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getStylesheets().add(getClass().getResource("Viper.css").toExternalForm());
        startColumn.setCellValueFactory(new PropertyValueFactory<>("base"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        processColumn.setCellValueFactory(new PropertyValueFactory("processName"));
        sizeColumn1.setCellValueFactory(new PropertyValueFactory<>("limit"));
        segmentColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        waitingProcess.setCellValueFactory(new PropertyValueFactory<>("name"));
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



    }






    public void addHoleButtonClicked() {

        String address = addInput.getText();
        String size = sizeInput.getText();

        processInput.getText();
        Node node = new Node(Integer.parseInt(address),Integer.parseInt(size),N.Hole,null);
        table.getItems().add(node);
        addInput.clear();
        sizeInput.clear();
        processInput.clear();


    }

    public void addSegmentButtonClicked() {

        String size = segmentSize.getText();
        Node node = new Node(0,Integer.parseInt(size),N.Segment,segmentName.getText(),null,processInput1.getText());

        table1.getItems().add(node);
        segmentSize.clear();
        segmentName.clear();
        processInput1.clear();


    }

    public void startOnAction() {

       /* if(  comboBox.getSelectionModel().getSelectedItem() == null)
            comboErrorLabel.setText("*Please Choose Algorithm First ");
        else if(table.getItems().size()==0)
            comboErrorLabel.setText("*Please Enter at least one Process ");
        else {
            XYChart.Series  series=new XYChart.Series();
            series.getData().add(new XYChart.Data(0,"", new GanttChart.ExtraData( Memory1.getMemorySize(),colors[16])));
            chart.getData().addAll(series);
            comboErrorLabel.setText("");
            switch ((String) comboBox.getValue()) {
                case "Best Fit":
                {
                    Pair<XYChart.Series,ArrayList<Process>> out= algotithem.BestFit(processes, Memory1);

                     series=out.getKey();

                    chart.getData().addAll(series);
                    ArrayList<Process> wait=out.getValue();
                    for(int i=0;i<wait.size();i++){
                        Waitings.add(wait.get(i));
                    }
                    if(wait.size()==0){
                        Waitings.clear();
                    }
                    break;

                }
                case "First Fit": // Priority(Non-Preemptive)
                {
                    Pair<XYChart.Series,ArrayList<Process>> out= algotithem.firstfit(processes, memroy);

                    series=out.getKey();

                    chart.getData().addAll(series);
                    ArrayList<Process> wait=out.getValue();
                    for(int i=0;i<wait.size();i++){
                        Waitings.add(wait.get(i));
                    }
                    if(wait.size()==0){
                        Waitings.clear();
                    }

                    break;

                }
                case "Worst Fit": // Priority(Non-Preemptive)
                {
                    Pair<XYChart.Series,ArrayList<Process>> out= algotithem.worstfit(processes, memroy);

                    series=out.getKey();

                    chart.getData().addAll(series);
                    ArrayList<Process> wait=out.getValue();
                    for(int i=0;i<wait.size();i++){
                        Waitings.add(wait.get(i));
                    }
                    if(wait.size()==0){
                        Waitings.clear();
                    }
                    break;
                }

            }
        }*/
    }

    public void deallocateProcessOnAction() {
        /*String name=ProcessName.getText();
        ProcessName.clear();
        int index=algotithem.is_name_process_exist(name,processes);
        if(index>=0){
            algotithem.deallocate_process(memroy,processes.get(index));
            algotithem.merge_holes(memroy);
            XYChart.Series  series=new XYChart.Series();
            series.getData().add(new XYChart.Data(0,"", new GanttChart.ExtraData( memroy.getMem_size(),colors[16])));
            chart.getData().addAll(series);
             series = algotithem.dreaw(memroy);
            chart.getData().addAll(series);

            processes=new ArrayList<>();



            for(int i=0;i<segments.size();i++) {
                Process p=new Process();
                p.setName(segments.get(i).getProcessName());
                if(p.getName().compareTo(name)==0) {
                    segments.remove(i);
                    i--;
                }
            }
            for(int i=0;i<memroy.getNum_segmant();i++){

                    if (memroy.getSegment(i).getProcessName().compareTo(name) == 0) {
                        memroy.remove_from_segments(i);
                        i--;
                    } else if(memroy.getSegment(i).getState().compareTo("newprocess")==0){
                        Segment hole = new Segment();
                        hole.setName("hole" + memroy.getNum_holes());
                        hole.setColor(colors[16]);
                        hole.setBase(memroy.getSegment(i).getBase());
                        hole.setLimit(memroy.getSegment(i).getLimit());
                        hole.setState("Hole");

                        memroy.setHoles(hole);
                        memroy.remove_from_segments(i);
                        i--;
                    }else{
                        memroy.remove_from_segments(i);
                        i--;
                    }

            }
            for(int i=0;i<segments.size();i++){
                Process p=new Process();
                p.setName(segments.get(i).getProcessName());
                 int index2 = inIndexOf(p.getName(), processes);
                    if (index2 >= 0) {
                        p.setSegments(segments.get(i));
                        processes.get(index2).setSegments(segments.get(i));
                    } else {
                        p.setSegments(segments.get(i));
                        processes.add(p);
                    }

            }

            algotithem.merge_holes(memroy);

        }else {
            //error
        }*/

    }

    public void DeallocateOldProcessOnAction() {
       /* int size=Integer.parseInt(sizeProcess.getText());
        int address=Integer.parseInt(adressProcess.getText());
        sizeProcess.clear();
        adressProcess.clear();

       algotithem.dealocate_seg(size,address,memroy,-1);
        XYChart.Series  series=new XYChart.Series();
        series.getData().add(new XYChart.Data(0,"", new GanttChart.ExtraData( memroy.getMem_size(),colors[16])));
        chart.getData().addAll(series);
          series=algotithem.dreaw(memroy);
         algotithem.merge_holes(memroy);

        chart.getData().addAll(series);





        for(int i=0;i<memroy.getNum_segmant();i++){


             if(memroy.getSegment(i).getState().compareTo("newprocess")==0){
                Segment hole = new Segment();
                hole.setName("hole" + memroy.getNum_holes());
                hole.setColor(colors[16]);
                hole.setBase(memroy.getSegment(i).getBase());
                hole.setLimit(memroy.getSegment(i).getLimit());
                hole.setState("Hole");

                memroy.setHoles(hole);
                memroy.remove_from_segments(i);
                i--;
            }else{
                memroy.remove_from_segments(i);
                i--;
            }

        }


        algotithem.merge_holes(memroy);*/


    }

    public void makeOnAction() {
       /* memroy=new Memroy();
        processes=new ArrayList<>();
        Waitings=WaitingTable.getItems();

            Waitings.clear();


        segments=table1.getItems();

        ObservableList<Segment> holes =  table.getItems();

        for(int i=0;i<holes.size();i++){
            holes.get(i).setState("Hole");
            holes.get(i).setColor(colors[16]);
            memroy.setHoles(new Segment(holes.get(i)));
        }
        memroy.setMem_size(Integer.parseInt(memorySizeInput.getText()));



        for(int i=0;i<segments.size();i++){
            Process p=new Process();
            p.setName(segments.get(i).getProcessName());
            int index=inIndexOf(p.getName(),processes);
            if(index>=0) {
                p.setSegments(segments.get(i));
                processes.get(index).setSegments(segments.get(i));
            }else{
                p.setSegments(segments.get(i));
                processes.add(p);
            }
        }
        int count=0;
        for (int i=0;i<processes.size();i++)
        {
            for(int j=0;j<processes.get(i).getNum_Segments();j++){
                processes.get(i).getSegment(j).setColor(colors[count]);
                count++;
            }
        }
        algotithem.merge_holes(memroy);*/

    }

    public void DeallocateSegmentOnAction() {
       /* int address=Integer.parseInt(segegmentName.getText());
        segegmentName.clear();
      int index=memroy.is_base_segment_exist(address);
      if(index>=0){
          Segment s=memroy.getSegment(index);
           int index2=in_Index_Of(s,segments);

          int size=memroy.getSegment(index).getLimit();

          XYChart.Series  series=new XYChart.Series();
          series.getData().add(new XYChart.Data(0,"", new GanttChart.ExtraData( memroy.getMem_size(),colors[16])));
          chart.getData().addAll(series);
          algotithem.dealocate_seg(size,address,memroy,index);
            series=algotithem.dreaw(memroy);
          algotithem.merge_holes(memroy);

          chart.getData().addAll(series);



          segments.remove(index2);
          processes=new ArrayList<>();
          for(int i=0;i<memroy.getNum_segmant();i++){


              if(memroy.getSegment(i).getState().compareTo("newprocess")==0){
                  Segment hole = new Segment();
                  hole.setName("hole" + memroy.getNum_holes());
                  hole.setColor(colors[16]);
                  hole.setBase(memroy.getSegment(i).getBase());
                  hole.setLimit(memroy.getSegment(i).getLimit());
                  hole.setState("Hole");

                  memroy.setHoles(hole);
                  memroy.remove_from_segments(i);
                  i--;
              }else{
                  memroy.remove_from_segments(i);
                  i--;
              }

          }
          for(int i=0;i<segments.size();i++){
              Process p=new Process();
              p.setName(segments.get(i).getProcessName());
              int index3 = inIndexOf(p.getName(), processes);
              if (index3 >= 0) {
                  p.setSegments(segments.get(i));
                  processes.get(index3).setSegments(segments.get(i));
              } else {
                  p.setSegments(segments.get(i));
                  processes.add(p);
              }

          }


          algotithem.merge_holes(memroy);



      }else{
          //error
      }*/

    }

    public void ExternalCompactionOnAction() {
       /* algotithem.External_compactio(memroy);
        XYChart.Series  series=new XYChart.Series();
        series.getData().add(new XYChart.Data(0,"", new GanttChart.ExtraData( memroy.getMem_size(),colors[16])));

        chart.getData().addAll(series);
       series=algotithem.drew_com(memroy);
        algotithem.merge_holes(memroy);
        chart.getData().addAll(series);
        for(int i=0;i<memroy.getNum_segmant();i++){
            if(memroy.getSegment(i).getState().compareTo("newprocess")==0){
                Segment hole = new Segment();
                hole.setName("hole" + memroy.getNum_holes());
                hole.setColor(colors[16]);
                hole.setBase(memroy.getSegment(i).getBase());
                hole.setLimit(memroy.getSegment(i).getLimit());
                hole.setState("Hole");

                memroy.setHoles(hole);
                memroy.remove_from_segments(i);
                i--;
            }else{
                memroy.remove_from_segments(i);
                i--;
            }
        }
        algotithem.merge_holes(memroy);*/
    }

    public void deleteHoleButtonClicked() {
        ObservableList<Node> productSelected, allProducts;
        allProducts = table.getItems();
        productSelected = table.getSelectionModel().getSelectedItems();

        productSelected.forEach(allProducts::remove);
    }

    public void resetHoleButtonClicked( ) {
        table.getItems().clear();
        chart.getData().clear();
    }

    public void deletSegmenteButtonClicked( ) {
        ObservableList<Node> productSelected, allProducts;
        allProducts = table1.getItems();
        productSelected = table1.getSelectionModel().getSelectedItems();

        productSelected.forEach(allProducts::remove);
    }

    public void resetSegmentButtonClicked( ) {
        table1.getItems().clear();
        chart.getData().clear();
        Waitings.clear();
    }
}
