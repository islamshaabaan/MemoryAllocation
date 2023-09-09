package sample;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class algotithem {
    public static String[] colors = {"status-darkRed", "status-green", "status-blue", "status-yellow", "status-black",
            "status-brown", "status-foshia", "status-bate5y", "status-smawy", "status-nescafe", "status-orange",
            "status-red", "status-lamony", "status-holoOrange", "status-purple", "status-move", "status-white"};

    public static void FirstFit(List<Process> NewProcs, Memory AllocMemory, List<Process> TotalProcs) {
        int flag = 0;
        for (int i = 0; i < NewProcs.size(); i++) {
            for (int j = 0; j < NewProcs.get(i).getSegmentsNumber(); j++) {
                if (AllocMemory.Fit(NewProcs.get(i).getNode(j).getSize(), NewProcs.get(i).getNode(j).getNodeName())) {
                    flag++;
                }
            }
            if (flag == NewProcs.get(i).getSegmentsNumber()) {
                System.out.println("Allocated");
            } else {
                System.out.println("Not Allocated");
                //AllocMemory.DeAllocate(i+ (OldProcs.size()),TotalProcs);
                String PrcName = NewProcs.get(i).getProcessName();
                AllocMemory.DeAllocate1(PrcName, TotalProcs);
                AllocMemory.Compaction();
            }
        }
    }

    public static void BestFit(List<Process> NewProcs, Memory AllocMemory, List<Process> TotalProcs) {
        int flag = 0;
        AllocMemory.SortingAsc();
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < NewProcs.size(); i++) {
            for (int j = 0; j < NewProcs.get(i).getSegmentsNumber(); j++) {
                if (AllocMemory.Fit(NewProcs.get(i).getNode(j).getSize(), NewProcs.get(i).getNode(j).getNodeName())) {
                    flag++;
                    AllocMemory.SortingAsc();
                }
            }
            if (flag == NewProcs.get(i).getSegmentsNumber()) {
                System.out.println("Allocated");
            } else {
                System.out.println("Not Allocated");
                //AllocMemory.DeAllocate(i+ (OldProcs.size()),TotalProcs);
                String PrcName = NewProcs.get(i).getProcessName();
                AllocMemory.DeAllocate1(PrcName, TotalProcs);
                AllocMemory.Compaction();
            }
        }
        /*for(int i=0;i<NewProcs.size();i++){
            for(int j=0; j<NewProcs.get(i).getSegmentsNumber(); j++){
                series.getData().add(new XYChart.Data(NewProcs.get(i).getNode(j).getStartingAdd(),"", new GanttChart.ExtraData( NewProcs.get(i).getNode(j).getFinishingAdd()-1,NewProcs.get(i).getNode(j).getColor()));
            }
        }*/
    }
    public static void WorstFit(List<Process> NewProcs, Memory AllocMemory, List<Process> TotalProcs) {
        AllocMemory.SortingDesSize(); int flag=0;
        for (int i = 0; i < NewProcs.size(); i++) {
            for (int j = 0; j < NewProcs.get(i).getSegmentsNumber(); j++) {
                if (AllocMemory.Fit(NewProcs.get(i).getNode(j).getSize(), NewProcs.get(i).getNode(j).getNodeName())) {
                    flag++;
                    AllocMemory.SortingDesSize();
                }
            }
            if (flag == NewProcs.get(i).getSegmentsNumber()) {
                System.out.println("Allocated");
            } else {
                System.out.println("Not Allocated");
                //AllocMemory.DeAllocate(i+ (OldProcs.size()),TotalProcs);
                String PrcName = NewProcs.get(i).getProcessName();
                AllocMemory.DeAllocate1(PrcName, TotalProcs);
                AllocMemory.Compaction();
            }
        }


    }


}
