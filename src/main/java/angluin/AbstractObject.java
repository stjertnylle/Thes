package angluin;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AbstractObject<T> {
    private final Method method;
    private final Object instance;
    protected T t;
    private Learner learner;

    public AbstractObject(Method method, Object instance){
        //this.alphabet = List.copyOf(alpha);
        this.instance = instance;
        this.method = method;
        this.t = (T) instance.getClass();
    }
    public Object useMethod(Object ... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }
//    public Object useMethod(char object) throws InvocationTargetException, IllegalAccessException {
//        return method.invoke(instance, object);
//    }
    public void setLearner(Learner learn){
        this.learner = learn;
    }
    public Class getReturnType(){
        return method.getReturnType();
    }
}
