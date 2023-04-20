import angluin.Learner;
import graph.*;
import org.checkerframework.checker.units.qual.A;
import trans.*;
import angluin.AbstractObject;
import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

public class main {
    public static void main(String[] args) throws IOException, ParseErrorException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        /*boolean rewrite = true;
        boolean bisim = true;
        boolean fairSim = true;
        Writer.Format format = Writer.Format.FSP;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("LTL formula?> ");
        String LTLin = in.readLine();
        Graph<String> g = translate(LTLin, rewrite, bisim, fairSim);
        Writer<String> w = Writer.getWriter (format, System.out);
        Writer<String> a = Writer.getWriter (Writer.Format.SPIN, System.out);
        w.write (g);
        a.write (g);*/

        URL myurl[] = { new URL("file:///C:/Users/morte/OneDrive/Documents/Skola/Thes/src/main/java/dummydata/")};
        URLClassLoader x = new URLClassLoader(myurl);
        Class loadedClass = x.loadClass("dummydata.DummyClass");
        Object instance = loadedClass.newInstance();
        Method methodIsEven = loadedClass.getMethod("isEven", String.class);
        Method methodReplace = loadedClass.getMethod("replace", String.class);
        Method methodMoreA = loadedClass.getMethod("moreA", String.class);
        LinkedList<Character> alphabetEven = new LinkedList<Character>();
        alphabetEven.add('a');
        alphabetEven.add('b');
        LinkedList<Character> alphabetReplace = new LinkedList<Character>();
        alphabetReplace.add('1');
        alphabetReplace.add('0');
        LinkedList<Character> alphabetMoreA = new LinkedList<>();
        alphabetMoreA.add('a');
        alphabetMoreA.add('b');
        //System.out.println(methodIsEven.invoke(instance, "haha"));
        AbstractObject learnerIsEven = new AbstractObject(methodIsEven, instance);
        AbstractObject learnerReplace = new AbstractObject(methodReplace, instance);
        AbstractObject learnerMore = new AbstractObject(methodMoreA, instance);
        //System.out.println(learnerIsEven.useMethod("knas"));
        //System.out.println(learnerReplace.useMethod("101101"));
        //Learner learnereven = new Learner(learnerIsEven, alphabetEven);
        Learner learnerMoreA = new Learner(learnerMore, alphabetMoreA);
        //Learner learnerrep = new Learner(learnerReplace, alphabetReplace);
    }

    public static Graph<String> translate(String formula, boolean rewrite,
                                          boolean bisim, boolean fair_sim) throws ParseErrorException {
        //	System.out.println("Translating formula: " + formula);
        // System.out.println();
        return translate(Parser.parse (formula), rewrite, bisim, fair_sim);
    }
    public static <PropT> Graph<PropT> translate(Formula<PropT> formula,
                                                 boolean rewrite, boolean bisim, boolean fair_sim) {
        if (rewrite) {
            formula = new Rewriter<PropT> (formula).rewrite();
        }
        Graph<PropT> gba = Translator.translate(formula);
        printStats(gba, "Generalized buchi automaton generated");
        gba = SuperSetReduction.reduce(gba);
        printStats(gba, "Superset reduction");
        Graph<PropT> ba = Degeneralize.degeneralize(gba);
        printStats(ba, "Degeneralized buchi automaton generated");
        ba = SCCReduction.reduce(ba);
        printStats(ba, "Strongly connected component reduction");
        if (bisim) {
            ba = Simplify.simplify(ba);
            printStats(ba, "Bisimulation applied");
        }
        if (fair_sim) {
            ba = SFSReduction.reduce(ba);
            printStats(ba, "Fair simulation applied");
        }
        reset_all_static();
        return ba;
    }
    public static <PropT> Graph<PropT> translate(Formula<PropT> formula) {
        return translate(formula, true, true, true);
    }

    public static Graph<String> translate(File file) throws ParseErrorException {
        String formula = "";

        try {
            LineNumberReader f = new LineNumberReader(new FileReader(file));
            formula = f.readLine().trim();
            f.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return translate(formula, true, true, true);
    }
    private static <PropT> void printStats(Graph<PropT> gba, String op) {
        System.out.println("\n***********************");
        System.out.println("\n" + op);
        System.out.println("\t" + gba.getNodeCount() + " states "
                + gba.getEdgeCount() + " transitions");
    }
    public static void reset_all_static() {
        Formula.resetStatic();
    }
}
