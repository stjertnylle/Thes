package angluin;


import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import static java.lang.Integer.*;

public class Table {

    private boolean tableClosed, tableConsistent, tableComplete;
    private LinkedList<LinkedList<String>> table;
    private LinkedList<Object> header;
    private LinkedList<LinkedList<String>> closedRows;
    private final Learner learner;
    private int longestSTR = 0;

    public Table(Learner learner) throws InvocationTargetException, IllegalAccessException {
        this.learner = learner;
        this.table = new LinkedList<>();
        this.closedRows = new LinkedList<>();
        this.tableClosed = false;
        this.tableConsistent = false;
        this.tableComplete = false;
        header = new LinkedList<>();
        header.add("\\");
        startTable();
    }

    private void startTable() throws InvocationTargetException, IllegalAccessException {
        //Init header
        header.add("");
        table.add(new LinkedList<>());
        table.get(table.size()-1).add("");
        String ob = learner.getResult("");
        table.get(table.size()-1).add(ob);

        //Init for every Letter in alphabet
        for(Object cha : learner.alphabet){
            table.add(new LinkedList<>());
            String charToRead = cha.toString();
            table.get(table.size()-1).add(charToRead);
            ob = learner.getResult(charToRead);
            table.get(table.size()-1).add(ob);
        }
        //run-loop
        while(!tableComplete){
            runTable();
        }
    }
    private void runTable() throws InvocationTargetException, IllegalAccessException {
        //System.out.println("\n +++++++++++++++++++ \n");
        //printTable();
        if(!tableClosed){
            //System.out.println("Kommer att stänga");
            closeTable();
            return;
        }
        if(!tableConsistent){
            //System.out.println("Kommer att consist");
            consistTable();
            return;
        }
        if(!tableComplete){
            System.out.println("Kommer att counter");
            Automaton autom = new Automaton(closedRows, buildSortedTable(), header);
            //System.out.println(autom.traverseFromStart("bab").isAcceptingBoolean());
            //System.out.println(autom.traverseFromStart("baa").isAcceptingBoolean());
            //System.out.println(autom.traverseFromStart("babba").isAcceptingBoolean());
            if(learner.isAutomatonCorrect(autom)){
                System.out.println("Success");
                autom.writeToFile();
                setTableComplete(true);
            }else{
                getCounterExample();
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("\n +++++++++++++++++++ \n");
    }

    private void getCounterExample() throws InvocationTargetException, IllegalAccessException {
        StringBuffer sb = learner.getNextCounter();
        System.out.println("Counter: " + sb.toString());
        if(sb == null){
            return;
        }
        /*while(sb.length() != 0){
            LinkedList list = rowContainingPrefix(closedRows, sb.toString());
            if(list == null){
                addRowToTable(sb.toString());
            }
            sb.deleteCharAt(sb.length()-1);
        }*/
        int i = 1;
        while(i <= sb.length()){
            if(rowContainingPrefix(closedRows, sb.substring(0, i)) == null){
                addRowToTable(sb.substring(0, i));
            }
            i++;
        }
        addNewCombinations();

        setTableConsistent(false);
        setTableClosed(false);
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

    private void consistTable() {
        LinkedList<LinkedList<String>> wholeTable = buildTable();
        for (int k = 0; k <= closedRows.size() - 1; k++) {
        //for (int k = closedRows.size() - 1; k >= 0; k--) {
            LinkedList list = closedRows.get(k);
            LinkedList otherList = getCopyOfRow(list);
            //System.out.println("!a är " + a);
            try{
                //System.out.println(list.get(0) + " | " + otherList.get(0));
            }catch (NullPointerException e){
                //System.out.println(list.get(0) + " | null");
            }
            if (otherList != null) {
                String firstPrefix = list.get(0).toString();
                String firstRowString = null;
                String secondRowString = null;
                String secondPrefix = otherList.get(0).toString();
                for (int i = 0; i < wholeTable.size(); i++) {
                    firstRowString = wholeTable.get(i).get(0).toString();
                    for (int j = 0; j < wholeTable.size(); j++) {
                        secondRowString = wholeTable.get(j).get(0).toString();
                        //System.out.println("Kollar rad + " + i + "(" + firstRowString + ")" + " mot rad " + j + "(" + secondRowString + ")");
                        if ((i != j) &&
                                (firstRowString.length() != firstPrefix.length()) &&
                                (secondRowString.length() != secondPrefix.length()) &&
                                (firstRowString.startsWith(firstPrefix)) &&
                                (secondRowString.startsWith(secondPrefix))) {
                            String firstEnding = firstRowString.substring(firstPrefix.length());
                            String secondEnding = secondRowString.substring(secondPrefix.length());
                            //System.out.println(secondEnding + " " + firstEnding);
                            //if (firstEnding.equals(secondEnding) && !isACopyOf(wholeTable.get(i), wholeTable.get(j))) {
                            if (firstEnding.equals(secondEnding) && !isACopyOf(wholeTable.get(i), wholeTable.get(j)) && !headerContains(firstEnding)) {
                                System.out.println("\"" + firstPrefix + "\"" + " " + firstRowString + " " + firstEnding + " | " +
                                        "\"" + secondPrefix + "\"" + " " + secondRowString + " " + secondEnding);
                                addColumnToTable(firstEnding);
                                System.out.println("Var inte consist");
                                setTableClosed(false);
                                setTableConsistent(false);
                                setTableComplete(false);
                                return;
                            }
                        }
                    }
                }
            }

        }
        setTableConsistent(true);
    }

    private void addColumnToTable(String firstEnding) {
        header.add(firstEnding);
        for(LinkedList list : closedRows){
            String prefix = list.get(0).toString();
            try {
                list.add(learner.getResult(prefix.concat(header.getLast().toString())));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for(LinkedList list : table){
            String prefix = list.get(0).toString();
            try {
                list.add(learner.getResult(prefix.concat(header.getLast().toString())));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private LinkedList<LinkedList<String>> buildTable() {
        LinkedList<LinkedList<String>> tempTable = new LinkedList<>();
        for(LinkedList<String> tempList: closedRows){
            tempTable.add(tempList);
        }
        for(LinkedList<String> tempList: table){
            tempTable.add(tempList);
        }
        return tempTable;
    }

    private LinkedList<LinkedList<String>> buildSortedTable(){
        LinkedList<LinkedList<String>> tempTable = new LinkedList<>();
        int closedTablePointer = 0;
        int restTablePointer = 0;
        while((closedTablePointer < closedRows.size()) || (restTablePointer < table.size())){
            int closeSize = MAX_VALUE;
            int tableSize = MAX_VALUE;
            try{
                closeSize = closedRows.get(closedTablePointer).get(0).length();
            }catch (IndexOutOfBoundsException e){
                tempTable.add(table.get(restTablePointer));
                restTablePointer += 1;
                continue;
            }try{
                tableSize = table.get(restTablePointer).get(0).length();
            }catch (IndexOutOfBoundsException e){
                tempTable.add(closedRows.get(closedTablePointer));
                closedTablePointer += 1;
                continue;
            }
            if( closeSize < tableSize){
                tempTable.add(closedRows.get(closedTablePointer));
                closedTablePointer += 1;
            }else if(closeSize > tableSize){
                tempTable.add(table.get(restTablePointer));
                restTablePointer += 1;
            }else{
                int compareOP = closedRows.get(closedTablePointer).get(0).compareTo(table.get(restTablePointer).get(0));
                if(compareOP == -1){
                    tempTable.add(closedRows.get(closedTablePointer));
                    closedTablePointer += 1;
                }else{
                    tempTable.add(table.get(restTablePointer));
                    restTablePointer += 1;
                }
            }
        }
        return tempTable;
    }

    public boolean headerContains(Object obj){
        return header.contains(obj);
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

    private boolean existCopyInClosedRows(LinkedList list) {
        for(LinkedList closedRow : closedRows){
            if(closedRow.get(0) == list.get(0)){
                break;
            }
            boolean match = true;
            for(int i = 1; i < closedRow.size() ; i++){
                if(closedRow.get(i) != list.get(i)){
                    match = false;
                    break;
                }
            }
            if(match){
                return true;
            }
        }
        return false;
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
            if(!existCopyInClosedRows(list)){
                moveRowUp(list);
                table.remove(list);
                setTableClosed(false);
                addNewCombinations();
                return;
            }
        }
        setTableClosed(true);
    }

    protected void addNewCombinations() throws InvocationTargetException, IllegalAccessException {
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
            for(Object other : learner.alphabet){
                if(!usedCombinations.contains(object.toString()+other.toString())){
                    table.add(new LinkedList<>());
                    table.getLast().add(object.toString()+other.toString());
                    for(int i = 1; i < header.size(); i++){
                        table.getLast().add(learner.getResult(table.getLast().get(0).toString() + header.get(i).toString()));
                    }
                    combinations.add(object.toString()+other.toString());
                }
            }
        }
    }

    protected LinkedList rowContainingPrefix(LinkedList<LinkedList<String>> megaList, String toString) {
        for(LinkedList list : megaList){
            if(list.get(0).equals(toString)){
                return list;
            }
        }
        return null;
    }

    protected void addRowToTable(String str) throws InvocationTargetException, IllegalAccessException {
        LinkedList list = rowContainingPrefix(table, str);
        if(list == null){
            closedRows.add(new LinkedList<>());
            closedRows.getLast().add(str);
            for(int i = 1; i < header.size(); i++){
                closedRows.getLast().add(learner.getResult(str + header.get(i)));
            }
        }else{
            moveRowUp(list);
        }
    }

    public void setTableClosed(boolean tableClosed) {
        this.tableClosed = tableClosed;
    }

    public void setTableConsistent(boolean tableConsistent) {
        this.tableConsistent = tableConsistent;
    }

    public void setTableComplete(boolean tableComplete) {
        this.tableComplete = tableComplete;
    }
}
