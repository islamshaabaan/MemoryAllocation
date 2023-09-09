package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Memory {
    private  int MemorySize;
    private Node Head;
    private Node Tail;
    private int NodesNumbers;
    private int tempSize;

    public void setMemorySize(int memsize){
        this.MemorySize=memsize;
    }
    public int getMemorySize(){
        return  MemorySize;
    }
    public void setHead(Node n){
        this.Head=n;
    }
    public Node getHead(){
        return Head;
    }
    public void setTail(Node n){
        this.Tail=n;
    }
    public Node getTail(){
        return Tail;
    }
    public void setNodesNumbers(int nNum){
        this.NodesNumbers=nNum;
    }
    public int getNodesNumbers(){
        return  NodesNumbers;
    }
    public void settempSize(int tmpsz){
        this.tempSize=tmpsz;
    }
    public int gettempSize(){
        return tempSize;
    }
    Memory (int size) {
        MemorySize = size;
        Node n = new Node (0, MemorySize,N.EmptyBlock,null);
        Head = Tail=n;
        NodesNumbers=0;
        tempSize=getMemorySize();
    }
    Memory () {
        MemorySize = 0;
        Node n = new Node (0, MemorySize,N.EmptyBlock,null);
        Head = Tail=n;
        NodesNumbers=0;
        tempSize=getMemorySize();
    }

    public void  PutHoles(int startingAdd, int size)
    {
        if(size<=getMemorySize() && gettempSize()>=size ) {
            // Put Old process at first node if the hole doesn't starting from 0
            if(startingAdd!=0 &&getHead().getNodeState()==N.EmptyBlock ) {
                Node hole2=new Node(0,startingAdd , N.OldProcess,"OldSeg",null,"OldProcess");
                setTail(hole2);
                setHead(getTail());
                setNodesNumbers(getNodesNumbers()+1);
                settempSize(gettempSize()-startingAdd);
            }
            Node hole1 = new Node(startingAdd, size, N.Hole, null);
            //Put hole in the first if it starting from 0
            if(getHead().getSize()==getMemorySize() && getHead().getNodeState()==N.EmptyBlock) {
                setTail(hole1);
                setHead(getTail());
                setNodesNumbers(getNodesNumbers()+1);
                settempSize(gettempSize()-size);
            }
            else {
                if(getTail().getFinishingAdd() +1 != hole1.getStartingAdd()) {
                    Node hole3 = new Node(getTail().getFinishingAdd ()+ 1, hole1.getStartingAdd() - (getTail().getFinishingAdd()+1),
                            N.OldProcess, "OldSegment" , null,"OldProcess");
                    Tail.setNext(hole3);
                    setTail(Tail.getNext());
                    settempSize(gettempSize()-(hole3.getSize()));
                    Tail.setNext( hole1);
                    setTail(Tail.getNext());
                    setNodesNumbers(getNodesNumbers()+2);
                    settempSize(gettempSize()-size);
                }
                else {
                    Tail.setNext( hole1);
                    setTail(Tail.getNext());
                    setNodesNumbers(getNodesNumbers()+1);
                    settempSize(gettempSize()-size);
                }
            }
        }
    }
    // Put Old Process at Last if there's NOT Hole at the last
    public void PutlastNode() {
        if(Tail.getNext()==null && gettempSize()>0) {
            Node hole4 = new Node(Tail.getFinishingAdd()+1 , getMemorySize() - (Tail.getFinishingAdd()+1),
                    N.OldProcess, "OldSegment",null,"OldProcess");
            Tail.setNext(hole4);
            setTail(Tail.getNext());
            setNodesNumbers(getNodesNumbers()+1);
            settempSize(gettempSize()- (getMemorySize() - (Tail.getFinishingAdd()+1)));

        }
    }

    public  boolean tryPlace(Node current, int NewSegSize,String name1)
    {
        // If we are not looking at a hole, or the hole is too small, abort.
        if (current.getNodeState()==N.Segment ||current.getNodeState()==N.OldProcess || current.getSize() < NewSegSize)
            return false;
        int remainingSpace = current.getSize() - NewSegSize;
        if (remainingSpace > 0) {
            // Create a new hole with the remaining space.
            Node remainingHole = new Node(current.getStartingAdd() + NewSegSize, remainingSpace, N.Hole, current.getNext());
            current.setSize(NewSegSize) ;
            current.setNodeState(N.Segment);
            current.setNodeName(name1);
            current.setFinishingAdd(current.getStartingAdd(),NewSegSize);
            current.setNext( remainingHole);
            setNodesNumbers(getNodesNumbers()+1);
            current.getNext().setFinishingAdd(current.getStartingAdd() + NewSegSize ,remainingSpace);
        }
        else {
            current.setSize ( NewSegSize);
            current.setNodeState(N.Segment);
            current.setNodeName(name1);
            current.setFinishingAdd(current.getStartingAdd(),NewSegSize);
        }
        return true;
    }
    public List StoreMemInList(List<Node> M)
    {
        for (Node n11 = Head; n11 != null; n11 = n11.getNext()) {
            if (n11.getNodeState() != N.Segment) {
                Node n1 = new Node(n11.getStartingAdd(), n11.getSize(), n11.getNodeState(), n11.getNodeName(),null,n11.getProcessName());
                M.add(n1);
            } else {
                Node n1 = new Node(n11.getStartingAdd(), n11.getSize(), n11.getNodeState(), n11.getNodeName(),null,n11.getProcessName());
                M.add(n1);
            }
        }
        return M;
    }
    public void StoreListInMem(List<Node> M) {
        for (int i = 0; i < M.size(); i++) {
            if (i == 0) {
                setTail(M.get(i));
                setHead(getTail());
            }
            else {
                Tail.setNext ( M.get(i));
                setTail ( Tail.getNext());
            }
        }
    }

    public boolean Fit(int Size, String name) {
        Node current=getHead();
        for(Node n=current; current!=null; current=current.getNext() ) {
            if (tryPlace(current, Size,name)) {
                return true;
            }
        }
        return false;
    }

    public void SortingAsc() {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);
        Collections.sort(SortedMemo);
        StoreListInMem(SortedMemo);
    }
    public  void SortingAscStartingAdd() {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);
        Collections.sort(SortedMemo, Node.NodeStartAddComparator);
        StoreListInMem(SortedMemo);

    }
    public  void SortingDesSize() {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);
        Collections.sort(SortedMemo, Node.DesSize);
        StoreListInMem(SortedMemo);
    }


    public void Compaction()
    {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);

        int SortMemSize=SortedMemo.size();            int num=0;
        for(int n=0 ; n<SortMemSize-1; n++ ){
            if(SortedMemo.get(num).getNodeState()==N.Hole && SortedMemo.get(num+1).getNodeState()==N.Hole &&
                    SortedMemo.get(num).getFinishingAdd()+1 == SortedMemo.get(num+1).getStartingAdd() ) {
                Node New=new Node(SortedMemo.get(num).getStartingAdd(),
                        SortedMemo.get(num).getSize() + SortedMemo.get(num+1).getSize() ,N.Hole,null);
                SortedMemo.remove(SortedMemo.get(num+1));
                SortedMemo.remove(SortedMemo.get(num));
                SortedMemo.add(New);
                setNodesNumbers(getNodesNumbers()-1);
                Collections.sort(SortedMemo,Node.NodeStartAddComparator);
            }
            else
                num++;
        }

        StoreListInMem(SortedMemo);
        SortingAscStartingAdd();

    }
    public void DeAllocate(int ProcessNumber, List<Process> Processes )
    {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);

        for(int i=0;i<Processes.get(ProcessNumber).getSegmentsNumber(); i++) {
            for(int n=0 ; n<SortedMemo.size(); n++ ){
                if ( (SortedMemo.get(n).getNodeState()== N.Segment||SortedMemo.get(n).getNodeState()== N.OldProcess) &&
                        SortedMemo.get(n).getNodeName() == Processes.get(ProcessNumber).getNode(i).getNodeName()) {
                    SortedMemo.get(n).setNodeState(N.Hole);
                    SortedMemo.get(n).setNodeName(null);
                    break;
                }
            }
        }

        StoreListInMem(SortedMemo);
    }
    public void DeAllocate1(String PName,List<Process> Processes){
        int ProcessNumber=-1;
        for(int i=0; i<Processes.size(); i++){
            if(Processes.get(i).getProcessName()==PName)
            {
                ProcessNumber=i;
                break;
            }
        }
        if(ProcessNumber!=-1) {
            DeAllocate(ProcessNumber,Processes);
        }
        else{
            System.out.println("Process is NOT exist");
        }
    }

    public void BestShuffling(List<Process> Processes) {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);
        List<Node> SortedMemo1 = new ArrayList<Node>();
        int newStartAdd=0;

        for (int i = 0; i < Processes.size(); i++) {
            for (int n = 0; n < Processes.get(i).getSegmentsNumber(); n++) {
                if (Processes.get(i).getNode(n).getNodeState() == N.Segment  ) {
                    Node new1 = new Node(newStartAdd, Processes.get(i).getNode(n).getSize(),
                            N.Segment, Processes.get(i).getNode(n).getNodeName(),null,Processes.get(i).getProcessName());
                    SortedMemo1.add(new1);
                    newStartAdd = new1.getFinishingAdd() + 1;
                }
            }
        }
        for(int n=0 ; n<SortedMemo.size(); n++ ){
            if(SortedMemo.get(n).getNodeState()==N.OldProcess){
                Node new1 = new Node(newStartAdd, SortedMemo.get(n).getSize(), N.OldProcess,"OldSegment",null,"OldProcess");
                SortedMemo1.add(new1);
                newStartAdd = new1.getFinishingAdd() + 1;
            }
        }
        int lastNodeFinishAdd=SortedMemo1.get( (SortedMemo1.size()-1) ).getFinishingAdd();
        Node TheBigHole=new Node( lastNodeFinishAdd+1 , getMemorySize() - (lastNodeFinishAdd+1), N.Hole,null);
        SortedMemo1.add(TheBigHole);
        StoreListInMem(SortedMemo1);
    }

    public void Shuffling()
    {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);
        List<Node> SortedMemo1 = new ArrayList<Node>();


        Collections.sort(SortedMemo,Node.NodeStartAddComparator);
        int SortMemSize=SortedMemo.size();
        int newStartAdd=0;
        for(int n=0 ; n<SortMemSize; n++ ){
            if(SortedMemo.get(n).getNodeState()==N.Segment ){
                Node new1=new Node(newStartAdd , SortedMemo.get(n).getSize() ,
                        N.Segment ,SortedMemo.get(n).getNodeName(),null,SortedMemo.get(n).getProcessName() );
                SortedMemo1.add(new1);
                newStartAdd= new1.getFinishingAdd() +1;
            }
            else if ( SortedMemo.get(n).getNodeState()==N.OldProcess){
                Node new1=new Node(newStartAdd , SortedMemo.get(n).getSize() , N.OldProcess,"OldSegment",null,"OldProcess" );
                SortedMemo1.add(new1);
                newStartAdd= new1.getFinishingAdd() +1;
            }
        }
        int lastNodeFinishAdd=SortedMemo1.get( (SortedMemo1.size()-1) ).getFinishingAdd();
        Node TheBigHole=new Node( lastNodeFinishAdd+1 , getMemorySize() - (lastNodeFinishAdd+1), N.Hole,null);
        SortedMemo1.add(TheBigHole);
        StoreListInMem(SortedMemo1);
    }

    public  boolean isEmpty() {
        if (getHead().getSize()==MemorySize && getHead().getNodeState()==N.EmptyBlock) {
            return true;
        }
        return false;
    }
}

