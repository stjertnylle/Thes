package angluin;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class Table {
    private boolean tableClosed;
    private boolean tableConsistent;
    private boolean tableComplete;
    private LinkedList<LinkedList<Object>> table;
    private LinkedList<Object> header;
    private LinkedList<LinkedList<Object>> closedRows;

    public Table(){
        this.table = new LinkedList<>();
        this.closedRows = new LinkedList<>();
        this.tableClosed = false;
        this.tableConsistent = false;
        this.tableComplete = false;
        header = new LinkedList<>();
        header.add("\\");

    }

    private void startTable() throws InvocationTargetException, IllegalAccessException {
        header.add("");
        table.add(new LinkedList<>());
        table.get(table.size()-1).add("");
        int ob = getResult("");
        table.get(table.size()-1).add(ob);
        closedRows.add(table.get(0));
        table.remove(0);
        for(Character cha : alphabet){
            table.add(new LinkedList<>());
            String charToRead = cha.toString();
            table.get(table.size()-1).add(charToRead);
            ob = getResult(charToRead);
            table.get(table.size()-1).add(ob);
        }
        while(!tableComplete){
            runTable();
        }
    }

    public void printTable() {
        for(Object object : header){
            System.out.print(object.toString() + "\t");
        }
        System.out.print("\n");
        for(LinkedList list : closedRows){
            for(Object object : list){
                System.out.print(object.toString() + "\t");
            }
            System.out.print("\n");
        }System.out.println("----------");
        for(LinkedList list : table){
            for(Object object : list){
                System.out.print(object.toString() + "\t");
            }
            System.out.print("\n");
        }
    }
}
