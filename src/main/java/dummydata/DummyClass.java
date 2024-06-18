package dummydata;

public class DummyClass {


    public boolean bIsSecondToLast(String str){
        //StringBuilder sb = new StringBuilder(str);
        if(str.length() < 2){
            return false;
        }
        return str.charAt(str.length()-2) == 'b';
        //return sb.charAt(sb.length()-2) == 'b';
    }

    public boolean doubleAInString(String str){
        int containsAA = str.indexOf("aa");
        int notContainsBB = str.indexOf("bb");
        return containsAA != -1 && notContainsBB == -1;
    }

    public boolean isEven(String str){
        char[] chars = str.toCharArray();
        if(str.equals("")){
            return true;
        }
        int a = 0;
        int b = 0;
        for(char c : chars){
            if(c == 'a'){
                a++;
            }else{
                b++;
            }
        }
        return a%2==0 && b%2==0;
    }

    public boolean multipleOf3(String str){
        char[] chars = str.toCharArray();
        int numberofAs = 0;
        for(char c : chars){
            if(c == 'a'){
                numberofAs++;
            }
        }
        return numberofAs % 3 == 0;
    }

    public static boolean isNumber(String str){
        if(str.length() == 0){
            return false;
        }
        char[] chars = str.toCharArray();
        boolean hasDecimalPoint = false;
        boolean lastWasDecimalPoint = false;
        boolean startedWithNumber = false;
        boolean lastWasZero = false;

        for(char c : chars){
            if(c == '.'){
                if(hasDecimalPoint){
                    return false;
                }
                hasDecimalPoint = true;
                lastWasDecimalPoint = true;
                lastWasZero = false;
            }
            else if(c == '0'){
                if (lastWasZero && !hasDecimalPoint && !startedWithNumber){
                    return false;
                }
                lastWasZero = true;
                lastWasDecimalPoint = false;
            }
            else if(Character.isDigit(c)){
                startedWithNumber = true;
                lastWasDecimalPoint = false;
                lastWasZero = false;
            }
            else{
                return false;
            }
        }
        if(lastWasDecimalPoint){
            return false;
        }
        return !hasDecimalPoint || !lastWasZero;
    }
}
