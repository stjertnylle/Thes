package angluin;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class Automaton {
    protected HashMap<String, Node> nodeMap = new HashMap<>();
    private Node startNode;


    public Automaton(LinkedList<LinkedList<String>> closedRows, LinkedList<LinkedList<String>> wholeTable, LinkedList<Object> header) {
        createNodes(closedRows);
        setupConnection(wholeTable, header);
        printNodes();
    }

    private void printNodes() {
        System.out.println("**NODER**");
        for(Node node : nodeMap.values()){
            node.printNode();
        }
        System.out.println("**NODER SLUT**");
    }

    private void setupConnection(LinkedList<LinkedList<String>> wholeTable, LinkedList<Object> header) {
        for(LinkedList row : wholeTable){
            startNode.setConnection(row, header);
        }
    }

    private void createNodes(List<LinkedList<String>> closedRowsCopy) {
        for (List list : closedRowsCopy) {
            StringBuilder nameOfNode = new StringBuilder("");
            for (int i = 1; i < list.size(); i++) {
                nameOfNode.append(list.get(i));
            }
            Node node = new Node(nameOfNode.toString(), this);
            if(nameOfNode.substring(0,1).equals("1")){
                node.setAccepting();
            }if(list.get(0).toString().isEmpty()){
                this.startNode = node.setAsStart();
            }
            nodeMap.putIfAbsent(nameOfNode.toString(), node);
        }
    }

    protected Node traverseFromStart(char[] nameOfNode){
        return startNode.traverse(nameOfNode);
    }
    protected Node traverseFromStart(String s){return traverseFromStart(s.toCharArray());}
    protected Node getNodeFromName(String nameOfNode){
        return nodeMap.get(nameOfNode);
    }

    public void writeToFile() {
        try {
            FileWriter myWriter = new FileWriter("filename.jff");
            myWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Created with JFLAP 6.4.--><structure>&#13;\n");
            myWriter.write("\t<type>fa</type>&#13;\n");
            myWriter.write("\t<automaton>&#13;\n");
            myWriter.write("\t\t<!--The list of states.-->&#13;\n");

            for(Node node : nodeMap.values()){
                myWriter.write("\t\t<state id=\"" + node.getName() + "\" name=\"" + node.getName() + "\">&#13;\n");
                myWriter.write("\t\t\t<x>50</x>&#13;\n");
                myWriter.write("\t\t\t<y>85.0</y>&#13;\n");
                if(node.isStart()){
                    myWriter.write("\t\t\t<initial/>&#13;\n");
                }
                if(node.isAcceptingBoolean()){
                    myWriter.write("\t\t\t<final/>&#13;\n");
                }
                myWriter.write("\t\t</state>&#13;\n");
            }
            //myWriter.write("</state>&#13;\n");
            myWriter.write("\t\t<!--The list of transitions.-->&#13;\n");
            for(Node node : nodeMap.values()){
                for(String s : node.destinations.keySet()){
                    myWriter.write("\t\t<transition>&#13;\n");
                    myWriter.write("\t\t\t<from>" + node.getName() + "</from>&#13;\n");
                    myWriter.write("\t\t\t<to>" + node.destinations.get(s).getName() + "</to>&#13;\n");
                    myWriter.write("\t\t\t<read>" + s + "</read>&#13;\n");
                    myWriter.write("\t\t</transition>&#13;\n");
                }
            }
            myWriter.write("\t</automaton>&#13;\n");
            myWriter.write("</structure>\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
