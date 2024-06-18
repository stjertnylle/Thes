package angluin;

import java.util.HashMap;
import java.util.LinkedList;

public class Node {
    public HashMap<String, Node> destinations = new HashMap<>();
    private boolean accepting, isTheStartNode;
    private Automaton automaton;
    private String name;

    public Node(String n, Automaton a){
        this.name = n;
        this.automaton = a;
    }

    void createConnection(String character, Node destination){
        destinations.putIfAbsent(character, destination);
    }

    public void setAccepting() {
        this.accepting = true;
    }
    public boolean isAcceptingBoolean(){
        return accepting;
    }
    protected String isAcceptingString(){return accepting ? "1" : "0";}
    public Node setAsStart(){
        isTheStartNode = true;
        return this;
    }

    public boolean isStart() { return  isTheStartNode;
    }
    public String getName(){
        return name;
    }

    public void setConnection(LinkedList row, LinkedList<Object> header) {
        String path = (String) row.get(0);
        if(path.isEmpty()){
            return;
        }
        StringBuilder destination = new StringBuilder();
        for (int i = 1; i < row.size(); i++){
            destination.append(row.get(i));
        }
        Node alreadyVisitedNode = automaton.traverseFromStart(path.substring(0, path.length()-1).toCharArray());
        if(alreadyVisitedNode != null){
            alreadyVisitedNode.createConnection(path.substring(path.length() -1), automaton.getNodeFromName(destination.toString()));
        }
        //System.out.println("kommer sätta " + path + " som " + alreadyVisitedNode.destinations.get(path.substring(path.length() - 1)).name);

    }

    public Node traverse(char[] rest) {
        //System.out.print("är i " + this.name + " med sträng ");
        if(rest.length == 0){
            //System.out.println("\t Returnerar true");
            return this;
        }else{
            String next = String.valueOf(rest[0]);
            char[] nextRest = new char[rest.length - 1];
            for(int i = 1 ; i < rest.length ; i++){
                nextRest[i-1] = rest[i];
            }
            //System.out.println("\t går in i " + next);
            try{
                return destinations.get(next).traverse(nextRest);
            }catch (NullPointerException e){
                return null;
            }
        }
    }

    public void printNode(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name);
        //sb.append("is start: " + isTheStartNode + "\n\t is accepting: " + isAcceptingBoolean() + "\n\t Destinations:");
        if(isTheStartNode){
            sb.append("\n\t INITIAL");
        }
        if(accepting){
            sb.append("\n\t ACCEPTING");
        }
        for(String s : destinations.keySet()){
            sb.append("\n\t\t" + s + " to " + destinations.get(s).getName());
        }
        System.out.println(sb.toString());
    }
}
