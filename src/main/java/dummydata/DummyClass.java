package dummydata;

public class DummyClass {

    public boolean moreA(String str){
        StringBuilder sb = new StringBuilder(str);
        if(sb.length() < 2){
            return false;
        }
        /*int a = 0;
        int b = 0;
        while(!sb.isEmpty()){
            if(sb.charAt(0) == 'a'){
                a++;
            }else{
                b++;
            }
            sb.deleteCharAt(0);
        }
        return b > a;*/
        return sb.charAt(sb.length()-2) == 'b';
    }

    public boolean isEven(String str){
        StringBuilder sb = new StringBuilder(str);
        if(str.equals("")){
            return false;
        }
        int a = 0;
        int b = 0;
        while(!sb.isEmpty()){
            if(sb.charAt(0) == 'a'){
                a++;
            }else{
                b++;
            }
            sb.deleteCharAt(0);
        }
        return a == b;
    }
    public String replace(String str){
        StringBuilder old = new StringBuilder(str);
        StringBuilder newSB = new StringBuilder();
        while(!old.isEmpty()){
            Character bla = old.charAt(0);
            Character etta = new Character('1');
            Character nolla = new Character('0');
            if(bla.equals(etta)){
                newSB.append(0);
            }else if(bla.equals(nolla)){
                newSB.append(1);
            }old.deleteCharAt(0);
        }
        return newSB.toString();
    }
}
