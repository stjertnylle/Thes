package angluin;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Random;

public class Learner {
    AbstractObject abstractObject;
    LinkedList<String> alphabet;
    private LinkedList<String> counterExamples = new LinkedList<>();
    private Table superTable;
    private final int NUMBEROFTRIES = 100;

    public Learner(AbstractObject ao, LinkedList<String> alpha) throws InvocationTargetException, IllegalAccessException {
        this.abstractObject = ao;
        this.alphabet = alpha;
        //counterExamples.add("bb");
        this.superTable = new Table(this);
        ao.setLearner(this);
    }

    protected String getResult(String character) throws InvocationTargetException, IllegalAccessException {
        if(abstractObject.useMethod(character).equals(true)){
            return "1";
        }return "0";
    }

    protected StringBuffer getNextCounter() throws InvocationTargetException, IllegalAccessException {
        if(counterExamples.isEmpty()){
            return null;
        }
        return new StringBuffer(counterExamples.remove(0));
    }


    public boolean isAutomatonCorrect(Automaton autom) throws InvocationTargetException, IllegalAccessException {
        int i = 0;
        while(i < NUMBEROFTRIES) {
            for (int j = 0; j < 10; j++) {
                String randomString = createRandomString(i).toString();
                //System.out.println(randomString);
                if (!getResult(randomString.toString()).equals(autom.traverseFromStart(randomString.toString()).isAcceptingString())) {
                    while (randomString.length() > 0) {
                        String tempString = randomString.substring(1);
                        if (!getResult(tempString.toString()).equals(autom.traverseFromStart(tempString.toString()).isAcceptingString())) {
                            randomString = tempString;
                        } else {
                            System.out.println(getResult(randomString.toString()) + " " + autom.traverseFromStart(randomString.toString()).isAcceptingString() + " " + randomString);
                            counterExamples.add(randomString.toString());
                            return false;
                        }
                    }
                }
            }
            i++;
        }
        return true;
    }

    private StringBuilder createRandomString(int lengthOfString) {
        StringBuilder sb = new StringBuilder("");
        int i = 0;
        while(i < lengthOfString){
            Random r = new Random();
            sb.append(alphabet.get(r.ints(0,alphabet.size()).findFirst().getAsInt()));
            i++;
        }
        return sb;
    }
}
