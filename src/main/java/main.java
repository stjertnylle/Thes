import angluin.Learner;
import dummydata.DummyClass;
import org.checkerframework.checker.units.qual.A;
import angluin.AbstractObject;
import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

public class main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        URL myurl[] = { new URL("file:///C:/Users/morte/OneDrive/Documents/Skola/Thes/src/main/java/dummydata/")};
        URLClassLoader x = new URLClassLoader(myurl);
        Class loadedClass = x.loadClass("dummydata.DummyClass");
        Object instance = loadedClass.newInstance();
        Method methodIsEven = loadedClass.getMethod("isEven", String.class);
        Method m_bIsSecondToLast = loadedClass.getMethod("bIsSecondToLast", String.class);
        Method m_aIsMultipleof3 = loadedClass.getMethod("multipleOf3", String.class);
        Method m_isNumber = loadedClass.getMethod("isNumber", String.class);
        Method m_contains = loadedClass.getMethod("doubleAInString", String.class);
        LinkedList<String> alphabetEven = new LinkedList<String>();
        alphabetEven.add("a");
        alphabetEven.add("b");
        LinkedList<String> alphabetMoreA = new LinkedList<>();
        alphabetMoreA.add("a");
        alphabetMoreA.add("b");
        LinkedList<String> alphabetMult3 = new LinkedList<>();
        alphabetMult3.add("a");
        alphabetMult3.add("b");
        LinkedList<String> alphabetDigit = new LinkedList<>();
        alphabetDigit.add(".");
        alphabetDigit.add("0");
        alphabetDigit.add("1");
        //alphabetDigit.add("2");
        //alphabetDigit.add("4");
        //alphabetDigit.add("5");
        //alphabetDigit.add("6");
        //alphabetDigit.add("7");
        //alphabetDigit.add("8");
        //alphabetDigit.add("9");
        //alphabetDigit.add(".");
        LinkedList<String> alphabetABC = new LinkedList<>();
        alphabetABC.add("a");
        alphabetABC.add("b");
        //alphabetABC.add("c");
        AbstractObject learnerIsEven = new AbstractObject(methodIsEven, instance);
        AbstractObject ao_bIsSecondToLast = new AbstractObject(m_bIsSecondToLast, instance);
        AbstractObject ao_multiple3 = new AbstractObject(m_aIsMultipleof3, instance);
        AbstractObject ao_number = new AbstractObject(m_isNumber, instance);
        AbstractObject ao_containsABC = new AbstractObject(m_contains, instance);

        //Learner l_bIsSecondToLast = new Learner(ao_bIsSecondToLast, alphabetMoreA);
        //Learner isEven = new Learner(learnerIsEven,alphabetMoreA);
        //Learner multi3 = new Learner(ao_multiple3, alphabetMult3);
        Learner numb = new Learner(ao_number, alphabetDigit);
        //Learner ABC = new Learner(ao_containsABC, alphabetABC);

    }
}
