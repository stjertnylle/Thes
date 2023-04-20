package angluin;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class Learner<T> {
    AbstractObject abstractObject;
    LinkedList<Character> alphabet;
    private boolean tableClosed;
    private boolean tableConsistent;
    private boolean tableComplete;
    private LinkedList<String> counterExamples = new LinkedList<>();
    private LinkedList<LinkedList<Object>> table;
    private LinkedList<Object> header;
    private LinkedList<LinkedList<Object>> closedRows;
    private Table superTable;

    public Learner(AbstractObject ao, LinkedList<Character> alpha) throws InvocationTargetException, IllegalAccessException {
        this.abstractObject = ao;
        this.alphabet = alpha;
        this.superTable = new Table();
        /*this.table = new LinkedList<>();
        this.closedRows = new LinkedList<>();
        this.tableClosed = false;
        this.tableConsistent = false;
        this.tableComplete = false;*/
        counterExamples.add("ba");
        ao.setLearner(this);
        //header = new LinkedList<>();
        //header.add("\\");
        startTable();
    }

    private void printTable() {
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

    private int getResult(String character) throws InvocationTargetException, IllegalAccessException {
        if(abstractObject.useMethod(character).equals(true)){
            return 1;
        }return 0;
    }

    private void runTable() throws InvocationTargetException, IllegalAccessException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printTable();
        System.out.println("\n +++++++ \n");
        while(!tableClosed){
            System.out.println("Kommer att stänga");
            closeTable();
            return;
        }
        while(!tableConsistent){
            System.out.println("Kommer att consist");
            consistTable();
            return;
        }
        if(!tableComplete){
            System.out.println("Kommer att counter");
            getCounterExample();
            return;
        }
    }

    private void getCounterExample() throws InvocationTargetException, IllegalAccessException {
        if(!counterExamples.isEmpty()){
            StringBuffer counter = new StringBuffer(counterExamples.remove(0));
            while(counter.length() != 0){
                LinkedList list = rowContainingPrefix(closedRows, counter.toString());
                if(list == null){
                    addRowToTable(counter.toString());
                }
                counter.deleteCharAt(counter.length()-1);
            }
            addNewCombinations();
        }
        tableConsistent = false;
        tableClosed = false;
    }

    private LinkedList rowContainingPrefix(LinkedList<LinkedList<Object>> megaList, String toString) {
        for(LinkedList list : megaList){
            if(list.get(0).equals(toString)){
                return list;
            }
        }
        return null;
    }

    private void addRowToTable(String str) throws InvocationTargetException, IllegalAccessException {
        LinkedList list = rowContainingPrefix(table, str);
        if(list == null){
            table.add(new LinkedList<>());
            table.getLast().add(str);
            for(int i = 1; i < header.size(); i++){
                table.getLast().add(getResult(str + header.get(i)));
            }
        }else{
            moveRowUp(list);
        }
    }

    private void consistTable() {
        for(LinkedList list : closedRows){
            LinkedList otherList = getCopyOfRow(list);
            if(otherList != null){
                System.out.println(list.toString() + " " + otherList.toString());
                break;
            }
        }
        tableConsistent = true;
    }

    private LinkedList getCopyOfRow(LinkedList list) {
        for(LinkedList closedRow : closedRows){
            if(isACopyOf(closedRow, list)){
                return closedRow;
            }
        }return null;
    }

    private boolean isACopyOf(LinkedList closedRow, LinkedList list) {
        if(closedRow.get(0) == list.get(0)){
            return false;
        }for(int i = 1; i < closedRow.size(); i++){
            if(closedRow.get(i) != list.get(i)){
                return false;
            }
        }return true;
    }

    private void moveRowUp(LinkedList list) {
        if(list == null){
            return;
        }
        closedRows.add(list);
        table.remove(list);
    }

    private void closeTable() throws InvocationTargetException, IllegalAccessException {
        for(LinkedList list : table){
            if(!existCopy(list)){
                //System.out.println(list.toString());
                moveRowUp(list);
                table.remove(list);
                this.tableClosed = false;
                addNewCombinations();
                return;
            }
        }
        this.tableClosed = true;
    }

    private void addNewCombinations() throws InvocationTargetException, IllegalAccessException {
        LinkedList<Object> closedPrefixes = new LinkedList<>();
        LinkedList<Object> combinations = new LinkedList<>();
        LinkedList<Object> usedCombinations = new LinkedList<>();
        for(LinkedList list : closedRows){
            closedPrefixes.add(list.get(0));
            usedCombinations.add(list.get(0));
        }
        for(LinkedList list : table){
            usedCombinations.add(list.get(0));
        }
        for(Object object : closedPrefixes){
            for(Object other : alphabet){
                if(!usedCombinations.contains(object.toString()+other.toString())){
                    table.add(new LinkedList<>());
                    table.getLast().add(object.toString()+other.toString());
                    for(int i = 1; i < header.size(); i++){
                        table.getLast().add(getResult(table.getLast().get(0).toString() + header.get(i).toString()));
                    }
                    combinations.add(object.toString()+other.toString());
                }
            }
        }
    }

    private boolean existCopy(LinkedList list) {
        for(LinkedList closedRow : closedRows){
            for(int i = 1; i < closedRow.size() ; i++){
                if((closedRow.get(i) == list.get(i)) && closedRow.get(0) != list.get(0)){
                    return true;
                }
            }
        }
        return false;
    }
}
