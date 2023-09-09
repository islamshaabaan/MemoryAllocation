package sample;

import java.util.*;

enum N
{
    OldProcess,Hole,Segment,EmptyBlock;
}

class Node implements Comparable<Node> {
    N NodeState;        // Equals false if this Node represents a hole
    int     StartingAdd;       // Position in memory of first byte
    int     size;        // Size that node occupies
    int     FinishingAdd;
    String SegmentName; // only if it's a segment
    Node    next;

    // Constructor for generating a hole or old process in memory
    Node (int locn, int sz, Node nxt , N state) {
        NodeState      = state;
        StartingAdd     = locn;
        size         = sz;
        FinishingAdd = locn+sz-1;
        next         = nxt;
    }
    // Constructor for generating a segment in memory
    Node (int locn, int sz, Node nxt , N state,String name) {
        NodeState      = state;
        StartingAdd     = locn;
        size         = sz;
        FinishingAdd = locn+sz-1;
        SegmentName = name;
        next         = nxt;
    }
    public int compareTo(Node compareNode) {
        int compareQuantity = ((Node) compareNode).size;
        //ascending order
        return this.size - compareQuantity;
        //descending order
        //return compareQuantity - this.quantity;
    }
    public static Comparator<Node> NodeStartAddComparator = new Comparator<Node>() {

        public int compare(Node node1, Node node2) {
            //ascending order
            return node1.StartingAdd - node2.StartingAdd;


        }
    };
    public static Comparator<Node> DesSize = new Comparator<Node>() {

            public int compare(Node node1,Node node2) {
            //descending order
            return node2.size-node1.size;
        }

    };
} // End Node class

//  Creates a linked-list of nodes to  memory segments and holes
class Memory {
    public  int MemorySize;  // Defines the total size for the memory
    Node Head;// Refers to first node in memory
    Node Tail;
    private static int tempSize;

    Memory (int size) {
        MemorySize = size;
        Node n = new Node (0, MemorySize,null,N.EmptyBlock);
        Head = Tail=n;
        tempSize=MemorySize;
    }

    public void  PutHoles(int startingAdd, int size)
    {
        if(size<=MemorySize && tempSize>=size ) {
            // Put Old process at first node if the hole doesn't starting from 0
            if(startingAdd!=0 &&Head.NodeState==N.EmptyBlock ) {
                Node hole2=new Node(0,startingAdd , null,N.OldProcess ,"OldSeg");
                Head=Tail=hole2;
                tempSize=tempSize- startingAdd;
            }
            Node hole1 = new Node(startingAdd, size, null, N.Hole);
            //Put hole in the first if it starting from 0
            if(Head.size==MemorySize && Head.NodeState==N.EmptyBlock) {
                Head = Tail=hole1;
                tempSize=tempSize-size;

            }
            else {
                if(Tail.FinishingAdd +1 != hole1.StartingAdd) {
                    Node hole3 = new Node(Tail.FinishingAdd + 1, hole1.StartingAdd - (Tail.FinishingAdd+1), null, N.OldProcess , "OldSeg");
                    Tail.next = hole3;
                    Tail = Tail.next;
                    tempSize= tempSize - (hole3.size);
                    Tail.next = hole1;
                    Tail = Tail.next;
                    tempSize= tempSize -(size);
                }
                else {
                    Tail.next=hole1;
                    Tail=Tail.next;
                    tempSize=tempSize-size;
                }
            }
        }
    }
    // Put Old Process at Last if there's NOT Hole at the last
    public void PutlastNode() {
        if(Tail.next==null && Memory.tempSize>0) {
            Node hole4 = new Node(Tail.FinishingAdd+1 , MemorySize - (Tail.FinishingAdd+1), null, N.OldProcess,"OldSeg");
            Tail.next = hole4;
            Tail = Tail.next;
        }
    }

   /* static void front(Node n){
        n.next = Head;
        Head = n;
    }
    static void insert(Node a, Node p){
        p.next = a.next;
        a.next = p;
    }*/

    public static boolean tryPlace(Node current, int NewSegSize,String name1)
    {
        // If we are not looking at a hole, or the hole is too small, abort.
        if (current.NodeState==N.Segment ||current.NodeState==N.OldProcess || current.size < NewSegSize)
            return false;
        int remainingSpace = current.size - NewSegSize;
        if (remainingSpace > 0) {
            // Create a new hole with the remaining space.
            Node remainingHole = new Node(current.StartingAdd + NewSegSize, remainingSpace, current.next, N.Hole);
            // current=new Node(current.StartingAdd,NewSegSize,remainingHole,N.Segment);
            current.size = NewSegSize;
            current.NodeState=N.Segment;
            current.SegmentName=name1;
            current.FinishingAdd=current.StartingAdd+NewSegSize-1;
            current.next = remainingHole;
            current.next.FinishingAdd=(current.StartingAdd + NewSegSize)+remainingSpace-1;
            //Tail = remainingHole;
        }
        else {
            //current=new Node(current.StartingAdd,NewSegSize,current.next,N.Segment);
            current.size = NewSegSize;
            current.NodeState=N.Segment;
            current.SegmentName=name1;
            current.FinishingAdd=current.StartingAdd+NewSegSize-1;
            //Tail=current;
        }
        return true;
    }
    public List StoreMemInList(List<Node> M)
    {
        for (Node n11 = Head; n11 != null; n11 = n11.next) {
            if (n11.NodeState != N.Segment) {
                Node n1 = new Node(n11.StartingAdd, n11.size, null, n11.NodeState , n11.SegmentName);
                M.add(n1);
            } else {
                Node n1 = new Node(n11.StartingAdd, n11.size, null, n11.NodeState, n11.SegmentName);
                M.add(n1);
            }
        }
        return M;
    }
    public void StoreListInMem(List<Node> M) {
        for (int i = 0; i < M.size(); i++) {
            if (i == 0)
                Head = Tail = M.get(i);
            else {
                Tail.next = M.get(i);
                Tail = Tail.next;
            }
        }
    }

    public boolean Fit(int Size, String name) {
        Node current=Head;
        for(Node n=current; current!=null; current=current.next ) {
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
            if(SortedMemo.get(num).NodeState==N.Hole && SortedMemo.get(num+1).NodeState==N.Hole &&
                    SortedMemo.get(num).FinishingAdd+1 == SortedMemo.get(num+1).StartingAdd ) {
                Node New=new Node(SortedMemo.get(num).StartingAdd,
                        SortedMemo.get(num).size + SortedMemo.get(num+1).size, null ,N.Hole);
                SortedMemo.remove(SortedMemo.get(num+1));
                SortedMemo.remove(SortedMemo.get(num));
                SortedMemo.add(New);
                Collections.sort(SortedMemo,Node.NodeStartAddComparator);
            }
            else
                num++;
        }

        StoreListInMem(SortedMemo);
        SortingAscStartingAdd();

    }
    public void DeAllocate(int ProcessNumber, List<List<Node>> Processes )
    {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);

        for(int i=0;i<Processes.get(ProcessNumber).size(); i++) {
            for(int n=0 ; n<SortedMemo.size(); n++ ){
                if ( (SortedMemo.get(n).NodeState== N.Segment||SortedMemo.get(n).NodeState== N.OldProcess) &&
                        SortedMemo.get(n).SegmentName == Processes.get(ProcessNumber).get(i).SegmentName) {
                    SortedMemo.get(n).NodeState=N.Hole;
                    SortedMemo.get(n).SegmentName=null;
                    break;
                }
            }
        }

        StoreListInMem(SortedMemo);
    }

    public void BestShuffling(List<List<Node>> Processes) {
        List<Node> SortedMemo = new ArrayList<Node>();
        StoreMemInList(SortedMemo);
        List<Node> SortedMemo1 = new ArrayList<Node>();
        int newStartAdd=0;

        for (int i = 0; i < Processes.size(); i++) {
            for (int n = 0; n < Processes.get(i).size(); n++) {
                if (Processes.get(i).get(n).NodeState == N.Segment  ) {
                    Node new1 = new Node(newStartAdd, Processes.get(i).get(n).size, null,
                            N.Segment, Processes.get(i).get(n).SegmentName);
                    SortedMemo1.add(new1);
                    newStartAdd = new1.FinishingAdd + 1;
                }
            }
        }
        for(int n=0 ; n<SortedMemo.size(); n++ ){
            if(SortedMemo.get(n).NodeState==N.OldProcess){
                Node new1 = new Node(newStartAdd, SortedMemo.get(n).size, null, N.OldProcess, "OldSeg");
                SortedMemo1.add(new1);
                newStartAdd = new1.FinishingAdd + 1;
            }
        }
        int lastNodeFinishAdd=SortedMemo1.get( (SortedMemo1.size()-1) ).FinishingAdd;
        Node TheBigHole=new Node( lastNodeFinishAdd+1 , MemorySize - (lastNodeFinishAdd+1),
                null , N.Hole);
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
            if(SortedMemo.get(n).NodeState==N.Segment ){
                Node new1=new Node(newStartAdd , SortedMemo.get(n).size , null,
                        N.Segment ,SortedMemo.get(n).SegmentName );
                SortedMemo1.add(new1);
                newStartAdd= new1.FinishingAdd +1;
            }
            else if ( SortedMemo.get(n).NodeState==N.OldProcess){
                Node new1=new Node(newStartAdd , SortedMemo.get(n).size , null,
                        N.OldProcess,"OldSeg" );
                SortedMemo1.add(new1);
                newStartAdd= new1.FinishingAdd +1;
            }
        }
        int lastNodeFinishAdd=SortedMemo1.get( (SortedMemo1.size()-1) ).FinishingAdd;
        Node TheBigHole=new Node( lastNodeFinishAdd+1 , MemorySize - (lastNodeFinishAdd+1),
                null , N.Hole);
        SortedMemo1.add(TheBigHole);

        StoreListInMem(SortedMemo1);
    }

    public  boolean isEmpty() {
        if (Head.size==MemorySize && Head.NodeState==N.EmptyBlock) {
            return true;
        }
        return false;
    }
}


public class MemAlloc {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Total Memory Size:");
        int TotalMemSize = sc.nextInt();
        Memory AllocMemory = new Memory(TotalMemSize);
        System.out.println("Holes Number:");
        int HolesNum = sc.nextInt();
        if (HolesNum == 0)
            AllocMemory.Head.NodeState = N.OldProcess;
        else {
            for (int h = 0; h < HolesNum; h++) {
                System.out.println("Hole Number:" + h);
                System.out.println("starting add:");
                int StartAdd = sc.nextInt();
                System.out.println("hole size:");
                int HoleSize = sc.nextInt();
                AllocMemory.PutHoles(StartAdd, HoleSize);
            }
            AllocMemory.PutlastNode();
        }
        List<Node> Memo = new ArrayList<Node>();
        AllocMemory.StoreMemInList(Memo);
        List<Node> OldProcesses = new ArrayList<Node>(Memo.size());
        //store old proc
        for (int n = 0; n < Memo.size(); n++) {
            if (Memo.get(n).NodeState == N.OldProcess) {
                OldProcesses.add(Memo.get(n));
            }
        }

        System.out.println("Process number:");
        int ProcNumber = sc.nextInt();
        List<List<Node>> NewProcesses = new ArrayList<List<Node>>(ProcNumber);
        for (int i = 0; i < ProcNumber; i++) {
            NewProcesses.add(new ArrayList<Node>());
        }
        List<List<Node>> TotalProcesses = new ArrayList<List<Node>>(ProcNumber + OldProcesses.size());
        for (int i = 0; i < ProcNumber + OldProcesses.size(); i++) {
            TotalProcesses.add(new ArrayList<Node>());
        }
        //Store Old Process in Total list
        for (int i = 0; i < OldProcesses.size(); i++) {
            TotalProcesses.get(i).add(OldProcesses.get(i));
        }

        int p=OldProcesses.size(); int p1=0;           int flag = 0;
        while(p1<ProcNumber) {
            System.out.println("Enter Process:P:" + p1);
            System.out.println("Segment number:");
            int SegNumber = sc.nextInt();
            for (int s = 0; s < SegNumber; s++) {
                System.out.println("Segment : " + s);
                System.out.println("Segment name ");
                String SegName = sc.next();
                System.out.println("Segment size:");
                int SegSize = sc.nextInt();
                Node segNode = new Node(0, SegSize, null, N.Segment, SegName);
                TotalProcesses.get(p).add(segNode);
                NewProcesses.get(p1).add(segNode);
            }
            p1++;
            p++;
        }

        //AllocMemory.Shuffling();

        System.out.println("Please Choose Allocation Method ");
        String AllocMethod = sc.next();

        AllocMemory.Compaction(); // IslamAhmed HabdHabd

        if(AllocMethod.equals("FirstFit")) {
            for (int i = 0; i < NewProcesses.size(); i++) {
                for (int j = 0; j < NewProcesses.get(i).size(); j++) {
                    if (AllocMemory.Fit(NewProcesses.get(i).get(j).size, NewProcesses.get(i).get(j).SegmentName)) {
                        flag++;
                    }
                }
                if (flag == NewProcesses.get(i).size()) {
                    System.out.println("Allocated");
                } else {
                    System.out.println("Not Allocated");
                    AllocMemory.DeAllocate(i+ (OldProcesses.size()),TotalProcesses);
                    AllocMemory.Compaction();
                }
            }
        }
        else if(AllocMethod.equals("BestFit")) {
            AllocMemory.SortingAsc();
            for (int i = 0; i < NewProcesses.size(); i++) {
                for (int j = 0; j < NewProcesses.get(i).size(); j++) {
                    if (AllocMemory.Fit(NewProcesses.get(i).get(j).size, NewProcesses.get(i).get(j).SegmentName)) {
                        flag++;
                        AllocMemory.SortingAsc();
                    }
                }
                if (flag == NewProcesses.get(i).size()) {
                    System.out.println("Allocated");
                } else {
                    System.out.println("Not Allocated");
                    AllocMemory.DeAllocate(i+ (OldProcesses.size()),TotalProcesses);
                    AllocMemory.Compaction();
                }
            }
        }
        else if(AllocMethod.equals("WorstFit")) {
            AllocMemory.SortingDesSize();
            for (int i = 0; i < NewProcesses.size(); i++) {
                for (int j = 0; j < NewProcesses.get(i).size(); j++) {
                    if (AllocMemory.Fit(NewProcesses.get(i).get(j).size, NewProcesses.get(i).get(j).SegmentName)) {
                        flag++;
                        AllocMemory.SortingDesSize();
                    }
                }
                if (flag == NewProcesses.get(i).size()) {
                    System.out.println("Allocated");
                } else {
                    System.out.println("Not Allocated");
                    AllocMemory.DeAllocate(i+ (OldProcesses.size()),TotalProcesses);
                    AllocMemory.Compaction();
                }
            }
        }

        AllocMemory.SortingAscStartingAdd();
        for(Node n=AllocMemory.Head; n!= null ; n=n.next) {
                System.out.println(+n.StartingAdd);
                System.out.println(n.NodeState);
                System.out.println(n.SegmentName);
                System.out.println(+n.FinishingAdd);
            }
        //sc.close();
    } // End main
} // End MemAlloc class