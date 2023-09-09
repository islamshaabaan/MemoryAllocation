package sample;

import java.util.ArrayList;
import java.util.List;

public class Process
{
    private String ProcessName;
    private int SegmentsNumber;
    private List<Node> SegmentsOfTheProcess = new ArrayList<Node>();

    public String getProcessName() {
        return ProcessName;
    }
    public void setProcessName(String prcName){
        this.ProcessName=prcName;
    }
    public int getSegmentsNumber() {
        return SegmentsNumber;
    }
    public void setSegmentsNumber(int segNum){
        this.SegmentsNumber=segNum;
    }
    public Node getNode(int index){
        return SegmentsOfTheProcess.get(index);
    }
    public void setNode(Node n){
        SegmentsOfTheProcess.add(n); SegmentsNumber++;
    }
    public Process(String name,int segnum,Node Old){ // only for old
        this.ProcessName=name;
        this.SegmentsNumber=segnum;
        SegmentsOfTheProcess.add(Old);
    }
    public Process(String name,List<Node> prcSegments){
        this.ProcessName=name;
        this.SegmentsOfTheProcess=prcSegments;
        this.SegmentsNumber=prcSegments.size();
    }
    public Process() {
        this.ProcessName = "";
        this.SegmentsNumber = 0;
        this.SegmentsOfTheProcess=null;
    }


}